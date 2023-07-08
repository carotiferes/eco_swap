package msUsers.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.MensajeRespuesta;
import msUsers.domain.entities.PropuestaSolicitud;
import msUsers.domain.entities.enums.EstadoPropuesta;
import msUsers.domain.repositories.MensajeRespuestaRepository;
import msUsers.domain.repositories.PropuestaSolicitudRepository;
import msUsers.domain.requests.RequestComunicarPropuestaSolicitudModel;
import msUsers.domain.requests.RequestMensajeRespuesta;
import msUsers.exceptions.handler.EntityNotFoundExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SolicitudService {

    @Autowired
    PropuestaSolicitudRepository propuestaSolicitudRepository;

    @Autowired
    MensajeRespuestaRepository mensajeRespuestaRepository;
    public void crearPropuestaComunicacion(RequestComunicarPropuestaSolicitudModel request, Long idSolicitud) {
        log.info(">> Service crear comunicacion de propuesta con request: {}", request.toString());
        PropuestaSolicitud propuestaSolicitud = PropuestaSolicitud.builder()
                .idSolicitud(idSolicitud)
                .idSwapper(request.getIdPerfilEmisor())
                .cantidadOfrecida(request.getSolicitudProductoModel().getCantidadOfrecida())
                .caracteristicas(request.getSolicitudProductoModel().getCaracteristicas())
                .fechaYHora(LocalDateTime.now())
                .tipoProducto(request.getSolicitudProductoModel().getTipoProducto())
                .mensaje(request.getSolicitudProductoModel().getMensaje())
                .imagenB64(request.getSolicitudProductoModel().getImagenB64())
                .estadoPropuesta(EstadoPropuesta.PENDIENTE)
                .build();
        propuestaSolicitudRepository.save(propuestaSolicitud);
        log.info("<< Propuesta de comunicacion guardado");
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
        if(request.getEstadoPropuesta()!= null) {
            propuestaSolicitud.setEstadoPropuesta(request.getEstadoPropuesta());
            if(request.getEstadoPropuesta().equals(EstadoPropuesta.RECIBIDA)) {
                //ACTULIZA LA SOLICITUD LA CANTDIDAD YA OBTENIDA
         //       propuestaSolicitud.set
            }
        }
        propuestaSolicitudRepository.save(propuestaSolicitud);
        log.info("<< Mensaje añadido a la lista de propuesta id {}", idSolicitud);
    }
}
