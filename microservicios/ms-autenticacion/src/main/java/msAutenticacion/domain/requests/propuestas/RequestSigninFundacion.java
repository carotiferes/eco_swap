package msAutenticacion.domain.requests.propuestas;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestSigninFundacion {

    @NotNull
    private String nombre;

    @NotNull
    private String cuil;

}
