package msUsers.domain.responses.logistica.resultResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultShippingService {

    private Long id;
    private String code;
    private String description;
    private Integer accepted_delay;
    private String type;
    private String category;
    private ResultCarrier carrier;

}
