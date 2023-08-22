package msUsers.domain.requests.donaciones;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import msUsers.domain.entities.enums.TipoProducto;

import java.util.List;


@Data
@Builder
@ToString(exclude = "imagenes")
public class RequestComunicarDonacionColectaModel {

    @NotNull(message = "Se debe seleccionar un tipo de producto.")
    private TipoProducto tipoProducto;

        @NotNull(message = "ProductoId debe tener un valor valido.")
        private Long productoId;

        @NotNull(message = "La cantidad ofrecida debe tener un valor valido.")
        @Positive(message = "La cantidad ofrecida debe ser mayor a cero.")
        private Integer cantidadOfrecida;

        private String mensaje;

        @NotNull(message = "Se necesita al menos una caracterìstica sobre el producto.")
        private List<String> caracteristicas;

        @NotNull(message = "Las donaciones deben contener imagenes.")
        private List<String> imagenes;

}
