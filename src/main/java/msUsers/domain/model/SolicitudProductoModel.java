package msUsers.domain.model;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;

@Builder
@Data
public class SolicitudProductoModel {

    private TipoProducto tipoProducto;
    private Integer cantidadOfrecida;
    private String mensaje;
    private String caracteristicas;
    private byte[] imagenB64;

}
