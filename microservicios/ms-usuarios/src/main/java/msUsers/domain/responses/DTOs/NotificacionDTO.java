package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.entities.enums.EstadoNotificacion;

@Data
public class NotificacionDTO {
    private long idNotificacion;
    private UsuarioDTO usuario;
    private String titulo;
    private String mensaje;
    private long idReferenciaNotificacion;
    private EstadoNotificacion estadoNotificacion;
}
