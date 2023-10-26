package msUsers.components;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import msUsers.components.events.NuevaDonacionEvent;
import msUsers.components.events.NuevaPropuestaTruequeEvent;
import msUsers.components.events.NuevoEstadoDonacionEvent;
import msUsers.components.events.NuevoEstadoTruequeEvent;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoNotificacion;
import msUsers.domain.entities.enums.TipoNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class NotificacionesListener {

    @Autowired
    private EntityManager entityManager;
    @EventListener
    public void handleNuevaDonacionEvent(NuevaDonacionEvent event) {
        Donacion donacion = event.getDonacion();
        Usuario usuario = event.getUsuario();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setIdReferenciaNotificacion(donacion.getIdDonacion());
        notificacion.setTitulo("¡Nueva donación!");
        notificacion.setMensaje("Tenés una nueva donación en la colecta: " + event.getColecta().getTitulo());
        notificacion.setTipoNotificacion(TipoNotificacion.DONACION);
        notificacion.setFechaNotificacion(LocalDate.now());
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
        notificacion.setIdReferenciaNotificacion(trueque.getIdTrueque());
        notificacion.setTitulo("¡Nueva propuesta de trueque!");
        notificacion.setMensaje("Tenés una nueva propuesta de trueque para tu publicación: " + publicacionOrigen.getTitulo());
        notificacion.setTipoNotificacion(TipoNotificacion.TRUEQUE);
        notificacion.setFechaNotificacion(LocalDate.now());
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
        String tituloPublicacion = donacion.getProducto().getPublicacion().getTitulo();
        Notificacion notificacion = new Notificacion();
        notificacion.setEstadoNotificacion(EstadoNotificacion.NO_LEIDO);
        notificacion.setIdReferenciaNotificacion(donacion.getIdDonacion());
        notificacion.setTitulo("Cambio de estado en tu donación.");
        notificacion.setMensaje("La donación " + tituloPublicacion + "que realizaste a la colecta " + colecta.getTitulo()
                + " cambió estado a " + event.getEstadoDonacion().toString().toLowerCase());
        notificacion.setTipoNotificacion(TipoNotificacion.NUEVO_ESTADO_DONACION);
        notificacion.setFechaNotificacion(LocalDate.now());
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
        notificacion.setIdReferenciaNotificacion(trueque.getIdTrueque());
        notificacion.setTitulo("Cambio de estado en tu trueque.");
        notificacion.setMensaje("El trueque de tu propuesta " + event.getPublicacion().getTitulo() + " cambio de estado a "
                + event.getEstadoTrueque().toString().toLowerCase());
        notificacion.setTipoNotificacion(TipoNotificacion.NUEVO_ESTADO_TRUEQUE);
        notificacion.setFechaNotificacion(LocalDate.now());
        notificacion.setUsuario(usuario);
        usuario.getNotificaciones().add(notificacion);
        entityManager.merge(usuario);
        log.info(">> Creada la notificación para el user: {}", usuario.getEmail());
    }
}
