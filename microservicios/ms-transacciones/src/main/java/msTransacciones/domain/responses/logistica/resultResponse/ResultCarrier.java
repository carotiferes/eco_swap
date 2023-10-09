package msTransacciones.domain.responses.logistica.resultResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultCarrier {

    private String code;
    private String description;
    private Boolean flexible_dispatching;
    private Long id;
    private String name;
    private String tracking_url;
}
