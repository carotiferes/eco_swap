package msUsers.domain.requests.logistica;

import lombok.Builder;
import lombok.Data;

@Data
public class PutOrderRequest {

    private String nuevoEstado;
}
