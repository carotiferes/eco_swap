package msTransacciones.domain.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msTransacciones.domain.entities.enums.TipoProducto;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestFilterColectas {
    private Long idFundacion;
    private String codigoPostal;
    private TipoProducto tipoProducto;

}