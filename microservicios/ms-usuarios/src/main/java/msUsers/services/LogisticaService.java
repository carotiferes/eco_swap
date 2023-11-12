package msUsers.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.client.shipnow.ResponseOrder;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.entities.enums.EstadoEnvio;
import msUsers.domain.logistica.Order;
import msUsers.domain.logistica.PingPong;
import msUsers.domain.logistica.enums.OrdenEstadoEnum;
import msUsers.domain.repositories.*;
import msUsers.domain.requests.logistica.PostOrderRequest;
import msUsers.domain.requests.logistica.PostProductosRequest;
import msUsers.domain.requests.logistica.PutOrderRequest;
import msUsers.domain.responses.logistica.resultResponse.ResultShippingOptions;
import msUsers.domain.responses.logisticaResponse.ResponseFechasEnvio;
import msUsers.domain.responses.logisticaResponse.ResponseOrdenDeEnvio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogisticaService {

    @Value("${logistica.url}")
    private String serviceUrl;

    @Value("${logistica.auth}")
    private String tokenAuth;

    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private ParticularesRepository particularesRepository;
    @Autowired
    private PublicacionesRepository publicacionesRepository;
    @Autowired
    private FundacionesRepository fundacionesRepository;
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private OrdenesRepository ordenesRepository;
    @Autowired
    private ColectasRepository colectasRepository;
    @Autowired
    private CriteriaBuilderQueries criteriaBuilderQueries;
    @Autowired
    private EntityManager entityManager;

    public PingPong pingpong() {
        HttpsURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(serviceUrl + "/ping");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{}";
            int responseCode = con.getResponseCode();
            log.info("-- SEND GET PING TO URL: {}", serviceUrl);
            log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            log.info("-- RESPONSE BODY: {}", response.toString());
            in.close();

            JsonObject convertedObject = new Gson().fromJson(response.toString(), JsonObject.class);

            return PingPong.builder()
                    .cache(String.valueOf(convertedObject.get("cache").getAsString()))
                    .db(String.valueOf(convertedObject.get("db").getAsString()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return PingPong.builder()
                    .cache("fail")
                    .db("fail")
                    .build();
        }
    }


    public List<ResponseOrdenDeEnvio> obtenerOrden(String userIdOrigen) {
        log.info(">> OBTENER ORDENES DE: {}", userIdOrigen);

        log.info(">> Obtener Detalles de ORDEN dado a OrdenId: {}", userIdOrigen);
        List<OrdenDeEnvio> listOrdenesObtenidasDeOrigen = ordenesRepository.findByIdUsuarioOrigen(Long.valueOf(userIdOrigen));
        List<OrdenDeEnvio> listOrdenesObtenidasComoDestinatario = ordenesRepository.findByIdUsuarioDestino(Long.valueOf(userIdOrigen));
        listOrdenesObtenidasDeOrigen.addAll(listOrdenesObtenidasComoDestinatario);
        List<ResponseOrdenDeEnvio> response = listOrdenesObtenidasDeOrigen.stream()
                .map(x -> ResponseOrdenDeEnvio.builder()
                        .orderId(String.valueOf(x.getIdOrden()))
                        .usernameDestino(x.getNombreUserDestino())
                        .listaFechaEstado(
                                x.getListaFechaEnvios().stream()
                                        .map(FechaEnvios::crearFechaEnvioResponse)
                                        .collect(Collectors.toList())
                        )
                        .build()
                )
                .toList();
        log.info("<< Orden de envio obtenida: {}", response.size());
        return response;
    }

    public List<OrdenDeEnvio> obtenerMisOrdenes(String userId, String type) {
        log.info(">> OBTENER ORDENES DE: {}", userId);
        List<OrdenDeEnvio> listOrdenesObtenidas;

        if(Objects.equals(type, "donaciones")) {
            log.info("donaciones del user origen: {}", userId);
            listOrdenesObtenidas = this.ordenesRepository.findByIdUsuarioOrigen(Long.valueOf(userId));
        } else {
            log.info("publicaciones del user destino: {}", userId);
            listOrdenesObtenidas = this.ordenesRepository.findByIdUsuarioDestino(Long.valueOf(userId));
        }
        log.info("<< Orden de envio obtenida: {}", listOrdenesObtenidas);
        return listOrdenesObtenidas;
    }

    @Transactional
    public void actualizarEstadoDeOrdenXOrdenId(String ordenId, PutOrderRequest putOrderRequest, Usuario usuario) throws Exception {
        //FALTA EL TEMA DE LOS OPTIONALS
        log.info(">> Actualizar el estado de OrdenId {} al estado: {}", ordenId, putOrderRequest.getNuevoEstado());
        OrdenDeEnvio orderAEnviar = ordenesRepository.findById(Long.valueOf(ordenId))
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrada la orden de envio: " + ordenId));
        if (usuario.getIdUsuario() != 999 || this.userPuedeHacerOrdenes(usuario, orderAEnviar)) {               // TODO: Solo cambié la negación del puedeHacerOrdenes (REVISAR)
            throw new Exception("No se tiene permiso para realizar modificación en la orden de envio.");
        }
        List<FechaEnvios> listadoFechasEnvios = orderAEnviar.getListaFechaEnvios();
        FechaEnvios ultimoEstado = listadoFechasEnvios.get(listadoFechasEnvios.isEmpty() ? 0 : listadoFechasEnvios.size() - 1);
        log.info(">> Cambio de estado de orden {} desde actual {} al nuevo {}", ordenId, ultimoEstado.getEstado().name(), putOrderRequest.getNuevoEstado());

        boolean esPublicacion = orderAEnviar.getEsPublicacion();

        LocalDate today = LocalDate.now();
        // - Custom Pattern
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(pattern);  //17-02-2022

        if(esPublicacion){

            String estado = putOrderRequest.getNuevoEstado();
            EstadoEnvio estadoEnvio;
            if(Objects.equals(estado, "RECIBIDO")) {
                estadoEnvio = EstadoEnvio.valueOf("RECIBIDA");
                Publicacion publicacion = publicacionesRepository.findById(orderAEnviar.getPublicacionId())
                        .orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicación: " + ordenId));
                publicacion.setEstadoEnvio(estadoEnvio);
                entityManager.merge(publicacion);
            }
        }
        else{
            String estado = putOrderRequest.getNuevoEstado();
            EstadoDonacion estadoDonacion;
            if(Objects.equals(estado, "RECIBIDO")) {
                estadoDonacion = EstadoDonacion.valueOf("RECIBIDA");
                orderAEnviar.getProductosADonarDeOrdenList().forEach(p -> setDonacionEstadoDonacion(estadoDonacion, p.getIdDonacion()));
                entityManager.merge(orderAEnviar);
            } //else orderAEnviar.getProductosADonarDeOrdenList().forEach(p -> setDonacionEstadoDonacion(EstadoDonacion.valueOf(putOrderRequest.getNuevoEstado()), p.getIdDonacion()));
        }

        FechaEnvios nuevaFechaEnvio = FechaEnvios.builder()
                .fechaEnvio(formattedDate)
                .estado(OrdenEstadoEnum.valueOf(putOrderRequest.getNuevoEstado()))
                .build();

        List<FechaEnvios> listadoFechasEnviosNuevo = orderAEnviar.getListaFechaEnvios();
        listadoFechasEnviosNuevo.add(nuevaFechaEnvio);
        orderAEnviar.setListaFechaEnvios(listadoFechasEnviosNuevo);
        ordenesRepository.save(orderAEnviar);
        log.info("<< Actualizacion de orden finalizada");

    }

    public OrdenDeEnvio obtenerDetallesDeOrdenXOrdenId(Long ordenId) throws Exception {
        log.info(">> Obtener Detalles de ORDEN dado a OrdenId: {}", ordenId);
        OrdenDeEnvio ordenObtenida = ordenesRepository.findById(ordenId).orElseThrow(Exception::new);
        log.info("<< Orden de envio obtenida: {}", ordenObtenida);
        return ordenObtenida;
    }

    public List<OrdenDeEnvio> obtenerDetallesDeOrdenXUsuarioId(Long userId) {
        log.info(">> Obtener listado de ORDENES de UserId: {}", userId);
        List<OrdenDeEnvio> ordenObtenida = ordenesRepository.findByIdUsuarioOrigen(userId);
        log.info("<< Listado de ordenes obtenidas para userId {} con un total de cantidad {}", userId, ordenObtenida.size());
        return ordenObtenida;
    }

    @Transactional
    public ResponseOrdenDeEnvio generarOrden(PostOrderRequest postOrderRequest) throws Exception {
        log.info(">> GENERAR ORDEN");
        OrdenDeEnvio ordenCreada = ordenesRepository.save(this.buildOrder(postOrderRequest));
        ResponseOrdenDeEnvio response = ResponseOrdenDeEnvio.builder()
                .usernameDestino(ordenCreada.getNombreUserDestino())
                .orderId(String.valueOf(ordenCreada.getIdOrden()))
                .listaFechaEstado(ordenCreada
                        .getListaFechaEnvios()
                        .stream()
                        .map(x -> ResponseFechasEnvio.builder()
                                .fecha(x.getFechaEnvio())
                                .estado(x.getEstado().name())
                                .build()
                        )
                        .collect(Collectors.toList()
                        )
                )
                .fechaADespachar(ordenCreada.getFechaADespachar())
                .build();
        //GENERAR CARTA DE ENVIO DE SHIPNOW
        log.info("<< ORDEN CREADA: {}", ordenCreada);
        return response;
    }

    public ResultShippingOptions getCostoEnvio(Long weight, Usuario usuario, String types) {
        HttpsURLConnection connection = null;
        try {
            String zipCode = usuario.getDirecciones().get(0).getCodigoPostal();
            //Create connection
            String uriParameters = "weight=" + weight.toString() + "&to_zip_code=" + zipCode + "&types=" + types;
            String urlString = serviceUrl + "/shipping_options?" + uriParameters;
            URL url = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            log.info("-- SEND GET PING TO URL: {}", serviceUrl);
            log.info("-- RESPONSE CODE: {}", responseCode);
            log.info("-- Shipnow Response: {}", con.getContent());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.toString());
            JsonNode firstOption = root.path("results").get(0); // Tomamos el primer envio de las opciones que vuelven
            ResultShippingOptions result = objectMapper.treeToValue(firstOption, ResultShippingOptions.class);

            log.info("-- RESPONSE BODY: {}", result.toString());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return ResultShippingOptions.builder()
                    .price(3500f)
                    .maximum_delivery(this.maximumDelivery())
                    .build();
        }
    }

    private String maximumDelivery() {
        LocalDate today = LocalDate.now();
        LocalDate maximumDelivery = today.plusDays(7);
        // - Custom Pattern
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return maximumDelivery.format(pattern);  //17-02-2022
    }

    @Transactional
    private OrdenDeEnvio buildOrder(PostOrderRequest postOrderRequest) throws Exception {

        try {

            final var usuarioOrigen = this.usuariosRepository.findById(postOrderRequest.getUserIdOrigen()).
                    orElseThrow(() -> new EntityNotFoundException("No fue encontrada el usuario de origen: " + postOrderRequest.getUserIdOrigen()));
            final var usuarioDestino = this.usuariosRepository.findById(postOrderRequest.getUserIdDestino()).
                    orElseThrow(() -> new EntityNotFoundException("No fue encontrada el usuario de destino: " + postOrderRequest.getUserIdDestino()));

            String nombreOrigen = this.obtenerNombreUser(usuarioOrigen.getIdUsuario(), usuarioOrigen.isSwapper());
            String nombreDestino = this.obtenerNombreUser(usuarioDestino.getIdUsuario(), usuarioDestino.isSwapper());

            Direccion direccionOrigen = usuarioOrigen.getDirecciones().get(0);
            Direccion direccionDestino = usuarioDestino.getDirecciones().get(0);
            List<PostProductosRequest> requestList = postOrderRequest.getListProductos();

            LocalDate today = LocalDate.now();
            // - Custom Pattern
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = today.format(pattern);  //17-02-2022

            boolean esPublicacion = postOrderRequest.getIdPublicacion() != null;

            OrdenDeEnvio orden = OrdenDeEnvio.builder()
                    .idUsuarioDestino(usuarioDestino.getIdUsuario())
                    .idUsuarioOrigen(usuarioOrigen.getIdUsuario())
                    .altura(direccionDestino.getAltura())
                    .barrio(direccionDestino.getLocalidad())
                    .ciudad("CABA")
                    .telefono(usuarioDestino.getTelefono())
                    .codigoPostal(direccionDestino.getCodigoPostal())
                    .esPublicacion(esPublicacion)
                    .piso(direccionDestino.getPiso())
                    .dpto(direccionDestino.getDpto())
                    .nombreCalle(direccionDestino.getCalle())
                    .nombreUserDestino(nombreDestino)
                    .nombreUserOrigen(nombreOrigen)
                    .titulo(postOrderRequest.getTitulo())
                    .precioEnvio(postOrderRequest.getCostoEnvio())
                    .listaFechaEnvios(List.of(FechaEnvios.builder()
                            .estado(OrdenEstadoEnum.POR_DESPACHAR)
                            .fechaEnvio(formattedDate)
                            .build()))
                    .fechaADespachar(this.crearFechaDespache())
                    .build();

            if(!esPublicacion) {
                List<ProductosADonarDeOrden> listaProductosAEnviar = requestList.stream().map(ProductosADonarDeOrden::crearProductoADonarRequest).toList();
                listaProductosAEnviar.forEach(p -> p.setOrdenDeEnvio(orden));
                orden.setProductosADonarDeOrdenList(listaProductosAEnviar);
                orden.setColectaId(postOrderRequest.getIdColecta());
                listaProductosAEnviar.forEach(p -> setDonacionEstadoDonacion(EstadoDonacion.EN_ENVIO, p.getIdDonacion()));
            } else{
                final var publicacion = this.publicacionesRepository.findById(postOrderRequest.getIdPublicacion()).
                        orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicacion: " + postOrderRequest.getIdPublicacion()));
                orden.setPublicacionId(postOrderRequest.getIdPublicacion());
                publicacion.setEstadoEnvio(EstadoEnvio.EN_ENVIO);
            }

            return orden;

        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw e;
        }

    }

    private String crearFechaDespache() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ) {
            calendar.add(Calendar.DATE, 3);
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {
            calendar.add(Calendar.DATE, 2);
        } else {
            calendar.add(Calendar.DATE, 1);
        }
        LocalDate localDate = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(dateTimeFormatter);
    }

    private String obtenerNombreUser(Long userId, Boolean isSwapper) {
        if (isSwapper) {
            Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(userId);
            Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("No hay un particular relacionado al id de usuario: " + userId));
            return particular.getNombre() + " " + particular.getApellido();
        } else {
            Optional<Fundacion> optionalFundacion = criteriaBuilderQueries.getFundacionPorUsuario(userId);
            Fundacion fundacion = optionalFundacion.orElseThrow(() -> new EntityNotFoundException("No hay una fundación relacionado al id de usuario: " + userId));
            return fundacion.getNombre();
        }
    }

    private String crearOrdenDeEnvioPDF(OrdenDeEnvio ordenDeEnvio) {


        return "";
    }

    private ResponseOrder buildOrder(Order order) {
        return ResponseOrder.builder()
                .timestamps(order.getTimestamps())
                .items(order.getItems())
                .comment(order.getComment())
                .id(order.getId())
                .status(order.getStatus())
                .last_updated_date(order.getTimestamps().getUpdated_at())
                .build();
    }

    private Boolean cancelarEnvio(String estadoNuevo, String estadoDeOrden) {
        if (estadoDeOrden.equals(OrdenEstadoEnum.POR_DESPACHAR.name()) ||
                estadoDeOrden.equals(OrdenEstadoEnum.ENVIADO.name()) ||
                estadoDeOrden.equals(OrdenEstadoEnum.RECIBIDO.name())
        ) {
            return estadoNuevo.equals(OrdenEstadoEnum.CANCELADO.name());
        }
        return false;
    }

    private Boolean userPuedeHacerOrdenes(Usuario user, OrdenDeEnvio ordenDeEnvio) {
        return ((user.getIdUsuario() == ordenDeEnvio.getIdUsuarioOrigen() &&
                ordenDeEnvio.getListaFechaEnvios().get(ordenDeEnvio.getListaFechaEnvios().size() - 1).getEstado().equals(OrdenEstadoEnum.ENVIADO))
                || (user.getIdUsuario() == ordenDeEnvio.getIdUsuarioDestino() &&
                ordenDeEnvio.getListaFechaEnvios().get(ordenDeEnvio.getListaFechaEnvios().size() - 1).getEstado().equals(OrdenEstadoEnum.RECIBIDO)));
    }

    private void setDonacionEstadoDonacion(EstadoDonacion estadoDonacion, Long idDonacion) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Donacion> query = cb.createQuery(Donacion.class);
        Root<Donacion> from = query.from(Donacion.class);
        Predicate predicate = cb.conjunction();

        predicate = cb.and(predicate, cb.equal(from.get("idDonacion"), idDonacion));

        query.where(predicate);

        Donacion donacion = entityManager.createQuery(query).getSingleResult();
        donacion.setEstadoDonacion(estadoDonacion);
        entityManager.merge(donacion);

    }
}
