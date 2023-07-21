package msAutenticacion.domain.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestLogin {

    @NotNull(message = "solicitudProductoModel Debe existir")
    private String username;

    @NotNull
    private String password;

}
