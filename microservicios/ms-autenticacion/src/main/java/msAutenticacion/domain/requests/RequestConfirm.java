package msAutenticacion.domain.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestConfirm {

    @NotNull(message = "Es necesario un id del usuario para la confirmación")
    private Long idUsuario;

    @NotNull(message = "Codigo es un campo obligatorio")
    @Size(max = 5, min = 5, message = "El código es incorrecto")
    private String codigo;
}
