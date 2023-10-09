package msTransacciones.domain.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Country {
    private String name;
    private String code;
}
