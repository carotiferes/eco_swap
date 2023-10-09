package msTransacciones.domain.logistica;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Dimension {

    private BigDecimal weight;
}
