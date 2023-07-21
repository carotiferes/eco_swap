package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.repositories.*;
import msUsers.domain.requests.donaciones.RequestComunicarDonacionColectaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ColectaService {

    @Autowired
    ColectaRepository colectaRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    ParticularRepository particularRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    DonacionesRepository donacionesRepository;

    @Autowired
    MensajeRespuestaRepository mensajeRespuestaRepository;
    @Autowired
    CaracteristicaPropuestaRepository caracteristicaPropuestaRepository;

    public void crearPropuestaComunicacion(RequestComunicarDonacionColectaModel request, Long idSolicitud) {
           log.info(">> SERVICE: Se comenzo la creacion de propuesta para la colecta: {}", idSolicitud);
           Colecta colecta = colectaRepository.findById(idSolicitud).get();
           Producto producto = colecta.getProductos().
                   stream()
                   .filter(x -> x.getIdProducto() == request.getColectaProductoModel().getProductoId())
                   .findAny().get();
           List<CaracteristicaPropuesta> lista = request.getColectaProductoModel()
                   .getCaracteristicas()
                   .stream()
                   .map(s -> CaracteristicaPropuesta.armarCarateristica(s, request.getIdParticular()))
                   .toList();
           Particular particular = particularRepository.findById(request.getIdParticular()).get();

           List<String> nombreImagenes = new ArrayList<>();
           for (int i = 0; i < request.getColectaProductoModel().getImagenes().size(); i++) {
               nombreImagenes.add(imageService.saveImage(request.getColectaProductoModel().getImagenes().get(i)));
           }

           Donacion donacionNueva = new Donacion();
           donacionNueva.setCantidadDonacion(request.getColectaProductoModel().getCantidadOfrecida());
           donacionNueva.setDescripcion(request.getColectaProductoModel().getMensaje());
           donacionNueva.setEstadoDonacion(EstadoDonacion.PENDIENTE);
           donacionNueva.setParticular(particular);
           donacionNueva.setProducto(producto);
           donacionNueva.setCaracteristicaPropuesta(lista);
           donacionNueva.setColecta(colecta);
           donacionNueva.setImagenes(String.join("|", nombreImagenes));


           log.info("<< Donacion creado con ID: {}", donacionNueva.getIdDonacion());
           List<Donacion> listaDonaciones = colecta.getDonaciones();
           log.info("<< Listado de propuestas originales de solciitud ID de propuestas: {}, cantidad original {}",
                   idSolicitud, listaDonaciones.size());
           listaDonaciones.add(donacionNueva);
           colecta.setDonaciones(listaDonaciones);
           var entity = colectaRepository.save(colecta);
           log.info("<< Colecta actualizado con ID de propuestas: {}",
                   listaDonaciones
                           .stream()
                           .map(Donacion::getIdDonacion)
                           .toList());
    }

    public List<Donacion> obtenerTodasLasPropuestasComunicacion(Long idSolicitud) {
        log.info(">> Obtener todas las propuestas de comunicacion de una Colecta: {}", idSolicitud);
        List<Donacion> donacionSolicitudes = colectaRepository.findById(idSolicitud).get().getDonaciones();
        log.info("<< Cantidad obtenidas: {}", donacionSolicitudes.size());
        return donacionSolicitudes;

    }

    public Donacion obtenerPropuestasComunicacionXId(Long idPropuestaComunicacion) {
        log.info(">> Obtener 1 propuesta de comunicacion de solicitud x id: {}", idPropuestaComunicacion);
        return donacionesRepository.findById(idPropuestaComunicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la comunicacion de propuesta con ID: "
                        + idPropuestaComunicacion));
    }
/* POSTERGAMOS ESTE DESARROLLO
    //PUEDE ACTUALIZAR LA PROPUESTA E INCLUSO LA SOLICITUD
    public void agregarMensajeParaPropuestaComunicacion(
            RequestMensajeRespuesta request, Long idSolicitud, Long idPropuesta) {
        log.info(">> Service crear mensaje para comunicacion de propuesta con request: {}", request.toString());

        Donacion propuesta = donacionesRepository.findById(idPropuesta).get();
        propuesta.setCaracteristicaPropuesta();

        MensajeRespuesta respuesta = caracteristicaPropuestaRepository.save(
                CaracteristicaPropuesta.builder()
            //            .fechaYHora(LocalDateTime.now())
           //             .idEmisor(request.getIdEmisor())
                        .mensaje(request.getMensaje())
                        .build()
        );
        log.info("<< Mensaje para comunicacion de propuesta con CREADO");
        caracteristicaPropuestaRepository.addNuevaRespuesta(respuesta);

        if(request.getEstadoDonacion()!= null) {
            propuestaSolicitud.setEstadoDonacion(request.getEstadoDonacion());
            if(request.getEstadoDonacion().equals(EstadoDonacion.RECIBIDA)) {
                //ACTULIZA LA SOLICITUD LA CANTDIDAD YA OBTENIDA
         //       propuestaSolicitud.set

                Colecta solicitud = colectaRepository.findById(idSolicitud).get();
                solicitud.setActiva(false);
                Producto productoAActualizar = solicitud.getProductos()
                        .stream()
                        .filter(producto ->
                                producto.getIdProducto()==propuestaSolicitud.getIdProducto())
                        .toList().get(0);
                Integer sumatoriaProductosSolicitados = productoAActualizar.getCantidadRecibida()
                        + propuestaSolicitud.getCantidadOfrecida();
                productoAActualizar.setCantidadRecibida(sumatoriaProductosSolicitados);
                colectaRepository.save(solicitud);
            }
        }
        propuestaSolicitudRepository.save(propuestaSolicitud);
        log.info("<< Mensaje añadido a la lista de propuesta id {}", idSolicitud);
    }
    */

}
