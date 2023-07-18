package msUsers.domain.requests;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;
import org.springframework.validation.annotation.Validated;

@Builder
@Data
@Validated
public class RequestProducto {

    @NotNull(message = "El producto debe tener un tipo que lo especifique.")
    private TipoProducto tipoProducto;

    @NotNull(message = "El producto debe poseer una descripción.")
    private String descripcion;

    @NotNull(message = "Se debe especificar una cantidad requerida.")
    @Positive(message = "La cantidad requerida debe ser mayor a cero.")
    private Integer cantidadRequerida;

    @NotNull(message = "El producto debe tener un estado asignado.")
    private String estado;

}
