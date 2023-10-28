package msUsers.domain.requests.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostProductosRequest {

    private Long productoId;
    private Long cantidad;
}
