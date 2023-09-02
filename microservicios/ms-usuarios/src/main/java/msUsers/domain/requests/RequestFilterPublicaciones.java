package msUsers.domain.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.entities.enums.TipoProducto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestFilterPublicaciones {
    private String codigoPostal;
    private TipoProducto tipoProducto;
}