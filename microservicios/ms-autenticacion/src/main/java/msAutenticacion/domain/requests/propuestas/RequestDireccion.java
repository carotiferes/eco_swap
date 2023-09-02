package msAutenticacion.domain.requests.propuestas;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Data
@Builder
public class RequestDireccion {

    @NotNull
    private String altura;

    @NotNull
    private String codigoPostal;

    @NotNull
    private String direccion;

    private String departamento;

    private String piso;
}