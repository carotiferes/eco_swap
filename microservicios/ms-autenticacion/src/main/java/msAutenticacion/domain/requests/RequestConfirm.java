package msAutenticacion.domain.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestConfirm {

    @NotNull(message = "username es un campo obligatorio")
    private String username;

    @NotNull(message = "Codigo es un campo obligatorio")
    @Size(max = 6, min = 6, message = "El código es el incorrecto")
    private String codigo;
}
