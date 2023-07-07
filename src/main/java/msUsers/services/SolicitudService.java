package msUsers.services;

import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.MensajeRespuesta;
import msUsers.domain.entities.PropuestaSolicitud;
import msUsers.domain.repositories.MensajeRespuestaRepository;
import msUsers.domain.repositories.PropuestaSolicitudRepository;
import msUsers.domain.requests.RequestComunicarPropuestaSolicitudModel;
import msUsers.domain.requests.RequestMensajeRespuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class SolicitudService {

    @Autowired
    PropuestaSolicitudRepository propuestaSolicitudRepository;

    @Autowired
    MensajeRespuestaRepository mensajeRespuestaRepository;
    public void crearPropuestaComunicacion(RequestComunicarPropuestaSolicitudModel request) {
        log.info(">> Service crear comunicacion de propuesta con request: {}", request.toString());
        PropuestaSolicitud propuestaSolicitud = PropuestaSolicitud.builder()
                .idSolicitud(request.getIdSolicitud())
                .idSwapper(request.getIdPerfilEmisor())
                .cantidadOfrecida(request.getSolicitudProductoModel().getCantidadOfrecida())
                .caracteristicas(request.getSolicitudProductoModel().getCaracteristicas())
                .fechaYHora(LocalDateTime.now())
                .tipoProducto(request.getSolicitudProductoModel().getTipoProducto())
                .mensaje(request.getSolicitudProductoModel().getMensaje())
                .imagenB64(request.getSolicitudProductoModel().getImagenB64())
                .build();
        propuestaSolicitudRepository.save(propuestaSolicitud);
        log.info("<< Propuesta de comunicacion guardado");
    }

    public List<PropuestaSolicitud> obtenerTodasLasPropuestasComunicacion(Long idSolicitud) {
        log.info(">> Obtener todas las propuestas de comunicacion de una Solicitud: {}", idSolicitud);
        return propuestaSolicitudRepository.findByIdSolicitud(idSolicitud);

    }

    public PropuestaSolicitud obtenerPropuestasComunicacionXId(Long idPropuestaComunicacion) {
        log.info(">> Obtener 1 propuesta de comunicacion de solicitud x id: {}", idPropuestaComunicacion);
        return propuestaSolicitudRepository.findById(idPropuestaComunicacion).get();
    }

    public void agregarMensajeParaPropuestaComunicacion(RequestMensajeRespuesta request) {
        log.info(">> Service crear mensaje para comunicacion de propuesta con request: {}", request.toString());

        PropuestaSolicitud propuestaSolicitud = this.obtenerPropuestasComunicacionXId(request.getIdComunicacion());
        mensajeRespuestaRepository.save(
                MensajeRespuesta.builder()
                        .fechaYHora(LocalDateTime.now())
                        .propuestaSolicitud(propuestaSolicitud)
                        .mensaje(request.getMensaje())
                        .build()
        );
        log.info("<< Mensaje para comunicacion de propuesta con CREADO");
    }
}
