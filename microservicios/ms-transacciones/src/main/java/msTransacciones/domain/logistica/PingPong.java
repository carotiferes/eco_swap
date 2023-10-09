package msTransacciones.domain.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PingPong {
    String cache;
    String db;
}
