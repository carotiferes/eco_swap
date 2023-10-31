package msUsers.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;
import msUsers.domain.client.shipnow.ResponseOrder;
import msUsers.domain.client.shipnow.ResponseOrderDetails;
import msUsers.domain.client.shipnow.response.ResponseGetListOrders;
import msUsers.domain.entities.*;
import msUsers.domain.logistica.Order;
import msUsers.domain.logistica.PingPong;
import msUsers.domain.logistica.enums.OrdenEstadoEnum;
import msUsers.domain.model.EstadoOrdenEnum;
import msUsers.domain.repositories.*;
import msUsers.domain.requests.logistica.PostOrderRequest;
import msUsers.domain.requests.logistica.PostProductosRequest;
import msUsers.domain.requests.logistica.PutOrderRequest;
import msUsers.domain.responses.ResponseShippingOptions;
import msUsers.domain.responses.logistica.resultResponse.ResultShippingOptions;
import msUsers.domain.responses.logisticaResponse.EnumEstadoOrden;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private FundacionesRepository fundacionesRepository;
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private OrdenesRepository ordenesRepository;
    @Autowired
    private ColectasRepository colectasRepository;


    public PingPong pingpong() {
        HttpsURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(serviceUrl+"/ping");
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
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
            while((inputLine = in.readLine()) != null) {
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
                .map(x-> ResponseOrdenDeEnvio.builder()
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

    private Boolean userPuedeHacerOrdenes(Usuario user, OrdenDeEnvio ordenDeEnvio) {
        return ((user.getIdUsuario()==ordenDeEnvio.getIdUsuarioOrigen() &&
            ordenDeEnvio.getListaFechaEnvios().get(ordenDeEnvio.getListaFechaEnvios().size()-1).getEstado().equals(OrdenEstadoEnum.ENVIADO))
            || (user.getIdUsuario()==ordenDeEnvio.getIdUsuarioDestino() &&
                ordenDeEnvio.getListaFechaEnvios().get(ordenDeEnvio.getListaFechaEnvios().size()-1).getEstado().equals(OrdenEstadoEnum.RECIBIDO)));
    }

    @Transactional
    public void actualizarEstadoDeOrdenXOrdenId(String ordenId, PutOrderRequest putOrderRequest, Usuario usuario) throws Exception {
        //FALTA EL TEMA DE LOS OPTIONALS
        log.info(">> Actualizar el estado de OrdenId {} al estado: {}", ordenId, putOrderRequest.getNuevoEstado());
        OrdenDeEnvio orderAEnviar = ordenesRepository.findById(Long.valueOf(ordenId)).get();
        if (usuario.getIdUsuario() != 999 || !this.userPuedeHacerOrdenes(usuario, orderAEnviar)) {
            throw new Exception("No se tiene permiso para realizar modificación en la orden de envio.");
        }
        List<FechaEnvios> listadoFechasEnvios = orderAEnviar.getListaFechaEnvios();
        FechaEnvios ultimoEstado = listadoFechasEnvios.get(listadoFechasEnvios.isEmpty() ? 0 : listadoFechasEnvios.size() - 1);
        log.info(">> Cambio de estado de orden {} desde actual {} al nuevo {}", ordenId, ultimoEstado.getEstado().name(), putOrderRequest.getNuevoEstado());
        if (this.cancelarEnvio(putOrderRequest.getNuevoEstado(), ultimoEstado.getEstado().name())) {
            //QUITAR LO RECIBIDO DEL PRODUCTO
            List<ProductosADonarDeOrden> listaProductos = orderAEnviar.getProductosADonarDeOrdenList();
            for (ProductosADonarDeOrden productoDeOrden : listaProductos) {
                Producto producto = productosRepository.findById(productoDeOrden.getIdProducto()).get();
                producto.setCantidadRecibida((int) (producto.getCantidadRecibida() - productoDeOrden.getCantidad()));
                productosRepository.save(producto);
            }
            //CAMBIAR LA DIRECCIÓN A DONDE ENVIAR
            /*
            Usuario usuarioOrigen = usuariosRepository.getReferenceById(orderAEnviar.getIdUsuarioOrigen());
            Direccion direccionOrigen = usuarioOrigen.getDirecciones().get(0);
            orderAEnviar.setCodigoPostal(direccionOrigen.getCodigoPostal());
            orderAEnviar.setDpto(direccionOrigen.getDpto());
            orderAEnviar.setAltura(direccionOrigen.getAltura());
            orderAEnviar.setNombreCalle(direccionOrigen.getCalle());
            orderAEnviar.setPiso(direccionOrigen.getPiso());
            String nombreDestino = orderAEnviar.getNombreUserDestino();
            orderAEnviar.setNombreUserDestino(orderAEnviar.getNombreUserOrigen());
            orderAEnviar.setNombreUserOrigen(nombreDestino);
            //SI USAMOS EL TEMA DE TICKETS DE JASPER, LO USAREMOS AQUI

             */
        }

        LocalDate today = LocalDate.now();
        // - Custom Pattern
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(pattern);  //17-02-2022

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
                        .map(x-> ResponseFechasEnvio.builder()
                                .fecha(x.getFechaEnvio())
                                .estado(x.getEstado().name())
                                .build()
                            )
                            .collect(Collectors.toList()
                        )
                )
                .build();
        //GENERAR CARTA DE ENVIO DE SHIPNOW
        log.info("<< ORDEN CREADA: {}", ordenCreada);
        return response;
    }

    public ResultShippingOptions getCostoEnvio(Long weight, String zipCode, String types) {
        HttpsURLConnection connection = null;
        try {
            //Create connection
            String uriParameters = "weight="+ weight.toString() + "&to_zip_code="+zipCode+"&types="+types;
            String urlString = serviceUrl+"/shipping_options?"+uriParameters;
            URL url = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenAuth);
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            log.info("-- SEND GET PING TO URL: {}", serviceUrl);
            log.info("-- RESPONSE CODE: {}", responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            log.info("-- RESPONSE BODY: {}", response.toString());

            in.close();

            ResponseShippingOptions result = new Gson().fromJson(response.toString(), ResponseShippingOptions.class);
            log.info("-- RESPONSE BODY: {}", result.toString());
            return result.getResults().get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    private OrdenDeEnvio buildOrder(PostOrderRequest postOrderRequest) throws Exception {

        try {


        Optional<Usuario> user = usuariosRepository.findById(postOrderRequest.getUserIdOrigen());
        Usuario usuarioOrigen = user.get();

        Usuario usuarioDestino = usuariosRepository.findById(postOrderRequest.getUserIdDestino()).get();

        String nombreOrigen = this.obtenerNombreUser(usuarioOrigen.getIdUsuario(), usuarioOrigen.isSwapper());
        String nombreDestino = this.obtenerNombreUser(usuarioDestino.getIdUsuario(), usuarioDestino.isSwapper());

        Direccion direccionOrigen = usuarioOrigen.getDirecciones().get(0);
        Direccion direccionDestino = usuarioDestino.getDirecciones().get(0);
        List<PostProductosRequest> requestList = postOrderRequest.getListProductos();
        if(postOrderRequest.getIdColecta()!=null) {
            Colecta colecta = colectasRepository.findById(postOrderRequest.getIdColecta()).get();

            //FILTRAMOS LOS PRODUCTOS QUE SI VAMOS A CONSIDERAR PARA RESTAR
            List<Producto> productosADonar = colecta.getProductos()
                    .stream()
                    .filter(x-> requestList.stream().anyMatch(xxx->xxx.getProductoId()==x.getIdProducto()))
                    .toList();
            for(Producto producto: productosADonar) {
                Long cantidad = requestList.stream()
                        .filter(x->x.getProductoId()== producto.getIdProducto())
                        .map(PostProductosRequest::getCantidad)
                        .toList()
                        .get(0);
                if(cantidad > (producto.getCantidadSolicitada()-producto.getCantidadRecibida())){
                    log.error("La intención de orden no es correcta");
                    throw new Exception("La cantidad a pedir es mayor que la cantidad solicitada restante");
                }
                producto.setCantidadRecibida((int) (producto.getCantidadRecibida()+cantidad));

                productosRepository.save(producto);
            }
        }
        LocalDate today = LocalDate.now();
        // - Custom Pattern
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(pattern);  //17-02-2022

        List<ProductosADonarDeOrden> listaProductosAEnviar = requestList.stream()
                .map(ProductosADonarDeOrden::crearProductoADonarRequest)
                .toList();
        return OrdenDeEnvio.builder()
                .idUsuarioDestino(usuarioDestino.getIdUsuario())
                .idUsuarioOrigen(usuarioOrigen.getIdUsuario())
                .altura(direccionDestino.getAltura())
                .codigoPostal(direccionDestino.getCodigoPostal())
                .piso(direccionDestino.getPiso())
                .dpto(direccionDestino.getDpto())
                .nombreCalle(direccionDestino.getCalle())
                .nombreUserDestino(nombreDestino)
                .nombreUserOrigen(nombreOrigen)
                .precioEnvio(postOrderRequest.getCostoEnvio())
                .productosADonarDeOrdenList(listaProductosAEnviar)
                .listaFechaEnvios(List.of(FechaEnvios.builder()
                                .estado(OrdenEstadoEnum.POR_DESPACHAR)
                                .fechaEnvio(formattedDate)
                        .build()))
                .publicacionId(postOrderRequest.getIdPublicacion())
                .colectaId(postOrderRequest.getIdColecta())
                .build();
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            throw e;
        }

    }

    private String obtenerNombreUser(Long userId, Boolean isSwapper) {
        if(isSwapper) {
            Particular particular = particularesRepository.findById(userId).get();
            return particular.getNombre() + " " + particular.getApellido();
        } else {
            Fundacion fundacion = fundacionesRepository.findById(userId).get();
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
        if(estadoDeOrden.equals(OrdenEstadoEnum.POR_DESPACHAR.name()) ||
                estadoDeOrden.equals(OrdenEstadoEnum.ENVIADO.name()) ||
                estadoDeOrden.equals(OrdenEstadoEnum.RECIBIDO.name())
        ) {
            return estadoNuevo.equals(OrdenEstadoEnum.CANCELADO.name());
        }
        return false;
    }
}
