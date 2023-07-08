package msUsers.domain.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.entities.enums.TipoProducto;

@Data
@Builder
public class RequestFilterSolicitudes {
    private Long idFundacion;
    private Long idPerfil; // Para la ubicación
    private TipoProducto tipoProducto;


}