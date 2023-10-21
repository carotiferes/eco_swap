package msUsers.domain.requests.logistica;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutOrderRequest {

    private String nuevoEstado;
}
