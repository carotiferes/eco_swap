package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import msUsers.domain.entities.enums.EstadoNotificacion;
import msUsers.domain.entities.enums.TipoNotificacion;
import msUsers.domain.responses.DTOs.NotificacionDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notificaciones")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private long idNotificacion;

    @ManyToOne
    private Usuario usuario;

    @NotNull
    private String titulo;

    @NotNull
    private String mensaje;

    @NotNull
    @Column(name = "id_referencia_notificacion")
    private long idReferenciaNotificacion;

    @Enumerated(value = EnumType.STRING)
    private EstadoNotificacion estadoNotificacion;

    @Enumerated(value = EnumType.STRING)
    private TipoNotificacion tipoNotificacion;

    private LocalDate fechaNotificacion;

    public NotificacionDTO toDTO(){
        NotificacionDTO notificacionDTO = new NotificacionDTO();
        notificacionDTO.setIdNotificacion(idNotificacion);
        notificacionDTO.setUsuario(usuario.toDTO());
        notificacionDTO.setMensaje(mensaje);
        notificacionDTO.setTitulo(titulo);
        notificacionDTO.setIdReferenciaNotificacion(idReferenciaNotificacion);
        notificacionDTO.setEstadoNotificacion(estadoNotificacion);
        notificacionDTO.setTipoNotificacion(tipoNotificacion);
        notificacionDTO.setFechaNotificacion(fechaNotificacion);
        return notificacionDTO;
    }
}
