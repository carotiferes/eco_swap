package msUsers.domain.client.shipnow;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VarianDimension {

    private Float weight;
    private Float height;
    private Float lenght;
    private Float width;
}
