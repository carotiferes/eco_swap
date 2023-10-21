package msUsers.domain.logistica;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Payment {

    private BigDecimal interest;
    private Integer shipping;
}
