package msAutenticacion.domain.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
public class RequestLogin {

    @NotNull(message = "El nombre de usuario es obligatorio.")
    private String username;

    @NotNull(message = "La contraseña es obligatoria.")
    private String password;

}
