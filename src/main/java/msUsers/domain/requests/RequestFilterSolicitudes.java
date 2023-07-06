package msUsers.domain.requests;

import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;

@Data
public class RequestFilterSolicitudes {
    private Long idFundacion;
    private String codigoPostal;
    private TipoProducto tipoProducto;
}