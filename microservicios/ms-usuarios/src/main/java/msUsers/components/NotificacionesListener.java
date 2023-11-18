package msUsers.components;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import msUsers.components.events.*;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.entities.enums.EstadoNotificacion;
import msUsers.domain.entities.enums.TipoNotificacion;
import msUsers.services.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class NotificacionesListener {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DonacionService donacionService;
    @EventListener
    public void handleNuevaDonacionEvent(NuevaDonacionEvent event) {
        Donacion donacion = event.getDonacion();
        Usuario usuario = event.getUsuario();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setIdReferenciaNotificacion(event.getColecta().getIdColecta());
        notificacion.setTitulo("¡Nueva donación!");
        notificacion.setMensaje("Recibiste una nueva donación en la colecta: " + event.getColecta().getTitulo());
        notificacion.setTipoNotificacion(TipoNotificacion.DONACION);
        notificacion.setFechaHoraNotificacion(LocalDateTime.now());
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        entityManager.merge(usuario);
        log.info(">> Creada la notificación para el user: {}", usuario.getEmail());
    }

    @EventListener
    public void handleNuevaPropuestaTrueque(NuevaPropuestaTruequeEvent event){
        Trueque trueque = event.getTrueque();
        Usuario usuario = event.getUsuario();
        Publicacion publicacionOrigen = event.getPublicacionOrigen();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setIdReferenciaNotificacion(trueque.getPublicacionOrigen().getIdPublicacion());
        notificacion.setTitulo("¡Nueva propuesta de trueque!");
        notificacion.setMensaje("Recibiste una nueva propuesta de trueque para tu publicación: " + publicacionOrigen.getTitulo());
        notificacion.setTipoNotificacion(TipoNotificacion.TRUEQUE);
        notificacion.setFechaHoraNotificacion(LocalDateTime.now());
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        entityManager.merge(usuario);
        log.info(">> Creada la notificación para el user: {}", usuario.getEmail());
    }

    @EventListener
    public void handleNuevoEstadoDonacionEvent(NuevoEstadoDonacionEvent event){
        Colecta colecta = event.getColecta();
        Donacion donacion = event.getDonacion();
        Usuario usuario = event.getUsuario();
        String tituloPublicacion = donacion.getProducto().getDescripcion();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setIdReferenciaNotificacion(event.getColecta().getIdColecta());

        switch(event.getEstadoDonacion()){
            case EN_ESPERA: // FUNDACION ESPERA QUE EL USUARIO LE LLEVE LA DONACION
                notificacion.setTitulo("¡Novedades en una donación aceptada!");
                notificacion.setMensaje("El usuario " + event.getDonacion().getParticular().getNombre() + " " + event.getDonacion().getParticular().getApellido()
                        + " llevará la donación de " + tituloPublicacion + " a la fundación.");
                break;
            case APROBADA:
                notificacion.setTitulo("¡Tu donación fue aprobada!");
                notificacion.setMensaje("La donación " + tituloPublicacion + " que enviaste a la colecta " + colecta.getTitulo()
                        + " fue aprobada. ¡Es hora de enviarlo!");
                break;
            default:
                notificacion.setTitulo("Cambio de estado en tu donación.");
                notificacion.setMensaje("La donación " + tituloPublicacion + " que realizaste a la colecta " + colecta.getTitulo()
                        + " cambió estado a " + event.getEstadoDonacion().toString().toLowerCase());
                break;
        }

        notificacion.setTipoNotificacion(TipoNotificacion.NUEVO_ESTADO_DONACION);
        notificacion.setFechaHoraNotificacion(LocalDateTime.now());
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        entityManager.merge(usuario);
        log.info(">> Creada la notificación para el user: {}", usuario.getEmail());
    }

    @EventListener
    public void handleNuevoEstadoTruequeEvent(NuevoEstadoTruequeEvent event){
        Trueque trueque = event.getTrueque();
        Usuario usuario = event.getUsuario();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setIdReferenciaNotificacion(trueque.getPublicacionOrigen().getIdPublicacion());
        notificacion.setTitulo("Cambio de estado en tu trueque.");
        notificacion.setMensaje("El trueque de tu propuesta " + event.getPublicacion().getTitulo() + " cambio de estado a "
                + event.getEstadoTrueque().toString().toLowerCase());
        notificacion.setTipoNotificacion(TipoNotificacion.NUEVO_ESTADO_TRUEQUE);
        notificacion.setFechaHoraNotificacion(LocalDateTime.now());
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        entityManager.merge(usuario);
        log.info(">> Creada la notificación para el user: {}", usuario.getEmail());
    }

    @EventListener
    public void handleNuevaCompraEvent(NuevaCompraEvent event){
        Compra compra = event.getCompra();
        Usuario usuario = event.getUsuario();
        Publicacion publicacion = event.getPublicacion();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setIdReferenciaNotificacion(compra.getIdCompra());
        notificacion.setTitulo("Compra de tu publicación " + publicacion.getTitulo());
        notificacion.setMensaje("El usuario " + compra.getParticularComprador().getUsuario().getUsername()
                + " compró tu publicación " + event.getPublicacion().getTitulo());
        notificacion.setTipoNotificacion(TipoNotificacion.NUEVA_COMPRA);
        notificacion.setFechaHoraNotificacion(LocalDateTime.now());
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        entityManager.merge(usuario);
        log.info(">> Creada la notificación para el user: {}", usuario.getEmail());
    }

    @EventListener
    public void handleNuevoEstadoOrdenEnvioEvent(NuevoEstadoOrdenEnvioEvent event){
        Usuario usuario = event.getUsuario();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setTipoNotificacion(TipoNotificacion.NUEVO_ESTADO_ORDEN_ENVIO);
        String tipoPublicacion;
        String tituloPublicacion;
        String entidad;

        StringBuilder msjBuilder = new StringBuilder();

        if(event.isEsPublicacion()){
            Publicacion publicacion = event.getPublicacion();
            tituloPublicacion = event.getPublicacion().getTitulo();
            tipoPublicacion = "publicacion";
            entidad = publicacion.getParticular().getNombre() + " " + publicacion.getParticular().getApellido();
            notificacion.setIdReferenciaNotificacion(publicacion.getIdPublicacion());
        }
        else{
            Donacion donacion = event.getDonacion();
            tituloPublicacion = event.getDonacion().getDescripcion();
            tipoPublicacion = "donación";
            entidad = this.donacionService.getColectaPorIdDonacion(donacion.getIdDonacion()).getFundacion().getNombre();
            notificacion.setIdReferenciaNotificacion(donacion.getIdDonacion());
        }

        switch(event.getEstadoEnvio()){
            case CANCELADO:
                notificacion.setTitulo("La orden de envio fue cancelada");
                msjBuilder.append("La orden de envio de la ").append(tipoPublicacion).append(" fue cancelada. Por favor, configurá otro envío.");
                break;
            case ENVIADO:
                notificacion.setTitulo("¡Tu " + tipoPublicacion + " ya esta en manos de Shipnow!");
                msjBuilder.append("La ").append(tipoPublicacion).append(" ").append(tituloPublicacion).append(" ya se encuentra en curso de envío.");
                break;
            case RECIBIDO:
                notificacion.setTitulo("¡Tu " + tipoPublicacion + " fue recibida!");
                msjBuilder.append("La ").append(tipoPublicacion).append(" ").append(tituloPublicacion).append(" fue recibida por ").append(entidad);
                break;
            default:
                notificacion.setTitulo("Cambio de estado en el envio de tu " + tipoPublicacion);
                msjBuilder.append("Tu ").append(tipoPublicacion).append(" cambió a ").append(event.getEstadoEnvio().toString());
                break;
        }

        notificacion.setMensaje(msjBuilder.toString());
        notificacion.setFechaHoraNotificacion(LocalDateTime.now());
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        entityManager.merge(usuario);
        log.info(">> Creada la notificación para el user: {}", usuario.getEmail());
    }
}
