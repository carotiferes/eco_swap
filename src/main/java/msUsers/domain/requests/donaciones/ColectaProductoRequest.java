package msUsers.domain.requests.donaciones;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import msUsers.domain.entities.enums.TipoProducto;

import java.util.List;

@Builder
@Data
@ToString(exclude = "imagenes")
public class ColectaProductoRequest {

    @NotNull(message = "TipoProducto debe tener un valor valido")
    private TipoProducto tipoProducto;

    @NotNull(message = "ProductoId debe tener un valor valido")
    private Long productoId;

    @NotNull(message = "cantidadOfrecida debe tener un valor valido")
    @Positive(message = "La cantidad ofrecida debe ser mayor a cero.")
    private Integer cantidadOfrecida;

    private String mensaje;

    @NotNull(message = "caracteristicas debe tener un valor string")
    private List<String> caracteristicas;

    @NotNull(message = "Las donaciones deben contener imagenes")
    private List<String> imagenes;

}
