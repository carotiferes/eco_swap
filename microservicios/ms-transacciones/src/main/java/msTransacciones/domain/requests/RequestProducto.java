package msTransacciones.domain.requests;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.entities.enums.TipoProducto;

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
