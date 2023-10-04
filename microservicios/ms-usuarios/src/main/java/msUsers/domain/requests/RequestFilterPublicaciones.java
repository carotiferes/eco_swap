package msUsers.domain.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.entities.enums.TipoProducto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestFilterPublicaciones {
    private List<String> localidades;
    private TipoProducto tipoProducto;
}
