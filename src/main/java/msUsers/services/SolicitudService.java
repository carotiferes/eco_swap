package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoPropuesta;
import msUsers.domain.repositories.*;
import msUsers.domain.requests.propuestas.RequestComunicarPropuestaSolicitudModel;
import msUsers.domain.requests.propuestas.RequestMensajeRespuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SolicitudService {

    @Autowired
    PropuestaSolicitudRepository propuestaSolicitudRepository;

    @Autowired
    SolicitudRepository solicitudRepository;

    @Autowired
    SwappersRepository swappersRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    PropuestasRepository propuestasRepository;

    @Autowired
    MensajeRespuestaRepository mensajeRespuestaRepository;
    @Autowired
    CaracteristicaPropuestaRepository caracteristicaPropuestaRepository;

    public void crearPropuestaComunicacion(RequestComunicarPropuestaSolicitudModel request, Long idSolicitud) {
        log.info(">> Service crear comunicacion de propuesta con request: {}", request.toString());

        Producto producto = productoRepository.findById(request.getSolicitudProductoModel().getProductoId()).get();
        Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();
        List<CaracteristicaPropuesta> lista = request.getSolicitudProductoModel().getCaracteristicas().stream()
                .map(s-> CaracteristicaPropuesta.armarCarateristica(s, 1l))
                .toList();
     //   Swapper swapper = swappersRepository.findById(request.getIdPerfilEmisor()).get();
        Propuesta propuestaNueva = Propuesta.builder()
                .cantidadPropuesta(request.getSolicitudProductoModel().getCantidadOfrecida())
                .descripcion(request.getSolicitudProductoModel().getMensaje())
                .estadoPropuesta(EstadoPropuesta.PENDIENTE)
                .swapper(null)
                .producto(producto)
                .caracteristicaPropuesta(lista)
                .solicitud(solicitud)
                /*
                .imagenes()
                 */

                .build();
        Propuesta creado = propuestasRepository.save(propuestaNueva);
        log.info("<< Propuesta creado con ID: {}", creado.getIdPropuesta());
    }

    public List<Propuesta> obtenerTodasLasPropuestasComunicacion(Long idSolicitud) {
        log.info(">> Obtener todas las propuestas de comunicacion de una Solicitud: {}", idSolicitud);
        List<Propuesta> propuestaSolicituds = solicitudRepository.findById(idSolicitud).get().getPropuestas();
        log.info("<< Cantidad obtenidas: {}", propuestaSolicituds.size());
        return propuestaSolicituds;

    }

    public Propuesta obtenerPropuestasComunicacionXId(Long idPropuestaComunicacion) {
        log.info(">> Obtener 1 propuesta de comunicacion de solicitud x id: {}", idPropuestaComunicacion);
        return propuestasRepository.findById(idPropuestaComunicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la comunicacion de propuesta con ID: "
                        + idPropuestaComunicacion));
    }
/* POSTERGAMOS ESTE DESARROLLO
    //PUEDE ACTUALIZAR LA PROPUESTA E INCLUSO LA SOLICITUD
    public void agregarMensajeParaPropuestaComunicacion(
            RequestMensajeRespuesta request, Long idSolicitud, Long idPropuesta) {
        log.info(">> Service crear mensaje para comunicacion de propuesta con request: {}", request.toString());

        Propuesta propuesta = propuestasRepository.findById(idPropuesta).get();
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

        if(request.getEstadoPropuesta()!= null) {
            propuestaSolicitud.setEstadoPropuesta(request.getEstadoPropuesta());
            if(request.getEstadoPropuesta().equals(EstadoPropuesta.RECIBIDA)) {
                //ACTULIZA LA SOLICITUD LA CANTDIDAD YA OBTENIDA
         //       propuestaSolicitud.set

                Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();
                solicitud.setActiva(false);
                Producto productoAActualizar = solicitud.getProductos()
                        .stream()
                        .filter(producto ->
                                producto.getIdProducto()==propuestaSolicitud.getIdProducto())
                        .toList().get(0);
                Integer sumatoriaProductosSolicitados = productoAActualizar.getCantidadRecibida()
                        + propuestaSolicitud.getCantidadOfrecida();
                productoAActualizar.setCantidadRecibida(sumatoriaProductosSolicitados);
                solicitudRepository.save(solicitud);
            }
        }
        propuestaSolicitudRepository.save(propuestaSolicitud);
        log.info("<< Mensaje añadido a la lista de propuesta id {}", idSolicitud);
    }
    */

}
