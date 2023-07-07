package msUsers.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;

@Builder
@Data
public class SolicitudProductoModel {

    @NotNull
    private TipoProducto tipoProducto;
    @NotNull
    private Integer cantidadOfrecida;
    @NotNull
    private String mensaje;
    @NotNull
    private String caracteristicas;
    private byte[] imagenB64;

}
