package msTransacciones.domain.responses.logistica.resultResponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResultShippingInfo {

    private Long id;
    private String name;
    private String operation_from_hour;
    private String operation_to_hour;
    private Boolean drop_off_point;
    private Boolean pickup_point;
    private String type;
}
