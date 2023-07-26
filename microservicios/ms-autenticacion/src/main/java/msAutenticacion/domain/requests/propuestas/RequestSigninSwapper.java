package msAutenticacion.domain.requests.propuestas;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RequestSigninSwapper {

    @NotNull
    private LocalDate fechaNacimiento;

    @NotNull
    private String dni;

    @NotNull
    private  String cuil;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    private String tipoDocumento;
}
