package msTransacciones.domain.client.shipnow;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VarianStock {

    private Integer available;
    private Integer committed;
    private Integer allocated;
    private Integer on_hand;
}
