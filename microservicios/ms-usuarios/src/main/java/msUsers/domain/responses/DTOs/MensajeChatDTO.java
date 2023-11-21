package msUsers.domain.responses.DTOs;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MensajeChatDTO {
    private UsuarioEnChatDTO usuarioEmisor;
    private UsuarioEnChatDTO usuarioReceptor;
    private String mensaje;
    private ZonedDateTime fechaHoraEnvio;
}
