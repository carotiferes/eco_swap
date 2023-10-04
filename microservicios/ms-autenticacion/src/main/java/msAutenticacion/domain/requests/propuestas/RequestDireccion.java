package msAutenticacion.domain.requests.propuestas;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
public class RequestDireccion {

    @NotNull(message = "La dirección debe tener una altura.")
    private String altura;

    @NotNull(message = "Se debe especificar una localidad.")
    private String localidad;

    @NotNull(message = "La dirección debe tener una calle.")
    private String calle;

    private String departamento;

    private String piso;
}
