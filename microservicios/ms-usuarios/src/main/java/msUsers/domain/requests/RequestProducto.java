package msUsers.domain.requests;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;
import org.springframework.validation.annotation.Validated;

@Builder
@Data
public class RequestProducto {

    @NotNull(message = "El producto debe tener un tipo que lo especifique.")
    private TipoProducto tipoProducto;

    @NotNull(message = "El producto debe poseer una descripción.")
    private String descripcion;

    private Integer cantidadRequerida;

    //ToDo: Chequear este estado (no figura como input en el front)
//    @NotNull(message = "El producto debe tener un estado asignado.")
      private String estado;

}
