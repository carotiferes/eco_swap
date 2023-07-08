package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.MensajeRespuesta;
import msUsers.domain.entities.PropuestaProductos;
import msUsers.domain.entities.PropuestaSolicitud;
import msUsers.domain.entities.Solicitud;
import msUsers.domain.entities.enums.EstadoPropuesta;
import msUsers.domain.repositories.MensajeRespuestaRepository;
import msUsers.domain.repositories.ProductosDePropuestaSolicitudRepository;
import msUsers.domain.repositories.PropuestaSolicitudRepository;
import msUsers.domain.repositories.SolicitudRepository;
import msUsers.domain.requests.propuestas.ProductoSolicitudRequest;
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
    ProductosDePropuestaSolicitudRepository productosDePropuestaSolicitudRepository;

    @Autowired
    MensajeRespuestaRepository mensajeRespuestaRepository;
    public void crearPropuestaComunicacion(RequestComunicarPropuestaSolicitudModel request, Long idSolicitud) {
        log.info(">> Service crear comunicacion de propuesta con request: {}", request.toString());
        List<ProductoSolicitudRequest> productosOfrecidos = request.getListadoProductos();
        if(productosOfrecidos == null || productosOfrecidos.size()== 0) {
            throw new EntityNotFoundException("No existen productos para ofrecer para la solicitud ID: "
                    + idSolicitud);
        }
        PropuestaSolicitud propuestaSolicitud = PropuestaSolicitud.builder()
                .idSolicitud(idSolicitud)
                .idSwapper(request.getIdPerfilEmisor())
                .cantidadOfrecida(request.getSolicitudProductoModel().getCantidadOfrecida())
                .caracteristicas(request.getSolicitudProductoModel().getCaracteristicas())
                .fechaYHora(LocalDateTime.now())
                .tipoProducto(request.getSolicitudProductoModel().getTipoProducto())
                .mensaje(request.getSolicitudProductoModel().getMensaje())
                .listadoPropuestaProductos(this.crearListadoProductosOfrecidos(productosOfrecidos))
                .estadoPropuesta(EstadoPropuesta.PENDIENTE)
                .build();
        PropuestaSolicitud creado = propuestaSolicitudRepository.save(propuestaSolicitud);
        log.info("<< Propuesta creado con ID: {}", creado.getId());
    }

    private List<PropuestaProductos> crearListadoProductosOfrecidos(List<ProductoSolicitudRequest> request) {
        return request.stream()
                .map(ProductoSolicitudRequest::toDomain)
                .collect(Collectors.toList());
    }

    public List<PropuestaSolicitud> obtenerTodasLasPropuestasComunicacion(Long idSolicitud) {
        log.info(">> Obtener todas las propuestas de comunicacion de una Solicitud: {}", idSolicitud);
        List<PropuestaSolicitud> propuestaSolicituds = propuestaSolicitudRepository.findAllByIdSolicitud(idSolicitud);
        log.info("<< Cantidad obtenidas: {}", propuestaSolicituds.size());
        return propuestaSolicituds;

    }

    public PropuestaSolicitud obtenerPropuestasComunicacionXId(Long idPropuestaComunicacion) {
        log.info(">> Obtener 1 propuesta de comunicacion de solicitud x id: {}", idPropuestaComunicacion);
        return propuestaSolicitudRepository.findById(idPropuestaComunicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la comunicacion de propuesta con ID: "
                        + idPropuestaComunicacion));
    }

    //PUEDE ACTUALIZAR LA PROPUESTA E INCLUSO LA SOLICITUD
    public void agregarMensajeParaPropuestaComunicacion(
            RequestMensajeRespuesta request, Long idSolicitud, Long idComunicacion) {
        log.info(">> Service crear mensaje para comunicacion de propuesta con request: {}", request.toString());

        //CHEQUEA PRIMERO LA PROPUESTA NO FUE CANCELADA O RECIBIDA
        PropuestaSolicitud propuestaSolicitud = this.obtenerPropuestasComunicacionXId(idComunicacion);
        if(propuestaSolicitud.getEstadoPropuesta().equals(EstadoPropuesta.RECIBIDA) ||
                propuestaSolicitud.getEstadoPropuesta().equals(EstadoPropuesta.CANCELADA)) {
            throw new EntityNotFoundException("La propuesta: " + idComunicacion +
                    " no puede recibir mas mensajes nuevos ya que su estado es: " + request.getEstadoPropuesta().toString());
        }
        MensajeRespuesta respuesta = mensajeRespuestaRepository.save(
                MensajeRespuesta.builder()
                        .fechaYHora(LocalDateTime.now())
                        .idEmisor(request.getIdEmisor())
                        .mensaje(request.getMensaje())
                        .build()
        );
        log.info("<< Mensaje para comunicacion de propuesta con CREADO");
        propuestaSolicitud.addNuevaRespuesta(respuesta);
        if(request.getListadoPropuestas()!=null && request.getListadoPropuestas().size()>0) {
            //SE ACTUALIZAN LOS ITEMS OFRECIDOS

            List<PropuestaProductos> listado = this.crearListadoProductosOfrecidos(request.getListadoPropuestas());
            listado.forEach(x-> productosDePropuestaSolicitudRepository.save(x));
        }
        if(request.getEstadoPropuesta()!= null) {
            propuestaSolicitud.setEstadoPropuesta(request.getEstadoPropuesta());
            if(request.getEstadoPropuesta().equals(EstadoPropuesta.RECIBIDA)) {
                //ACTULIZA LA SOLICITUD LA CANTDIDAD YA OBTENIDA
         //       propuestaSolicitud.set

                Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();
                solicitud.setActiva(false);
                solicitudRepository.save(solicitud);
            }
        }
        propuestaSolicitudRepository.save(propuestaSolicitud);
        log.info("<< Mensaje añadido a la lista de propuesta id {}", idSolicitud);
    }
}
