package msUsers.domain.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestMensajeChat {
    @NotNull(message = "Un mensaje debe estar asociado a un trueque.")
    private Long idTrueque;
    @NotNull(message = "Se debe enviar al menos un mensaje.")
    @NotBlank(message = "El mensaje no debe estar vacío.")
    private String mensaje;
    @NotNull(message = "El mensaje debe ser recibido por usuario válido.")
    private Long usuarioReceptor;
}
