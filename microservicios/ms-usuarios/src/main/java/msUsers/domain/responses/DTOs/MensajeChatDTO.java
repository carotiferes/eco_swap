package msUsers.domain.responses.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MensajeChatDTO {
    private UsuarioEnChatDTO usuarioEmisor;
    private UsuarioEnChatDTO usuarioReceptor;
    private String mensaje;
    private LocalDateTime fechaHoraEnvio;
}
