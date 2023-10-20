package msUsers.domain.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Stock {

    private Integer on_hand;
    private Integer avaible;
    private Integer allocated;
    private Integer committed;
}
