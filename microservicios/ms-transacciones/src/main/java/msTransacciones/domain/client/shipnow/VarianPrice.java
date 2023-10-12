package msTransacciones.domain.client.shipnow;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VarianPrice {

    private String retail;
    private String wholesale;
    private String buy;
}
