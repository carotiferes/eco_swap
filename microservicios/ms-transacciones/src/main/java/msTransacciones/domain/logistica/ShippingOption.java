package msTransacciones.domain.logistica;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ShippingOption {

    private String service_code;
    private String carrier_code;
    private String category;
    private BigDecimal price;
    private String service_type;
}
