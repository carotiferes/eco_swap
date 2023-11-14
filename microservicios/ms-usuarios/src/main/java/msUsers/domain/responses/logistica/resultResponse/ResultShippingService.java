package msUsers.domain.responses.logistica.resultResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String mode;
    private ResultCarrier carrier;

    @JsonCreator
    public ResultShippingService(
            @JsonProperty("id") Long id,
            @JsonProperty("code") String code,
            @JsonProperty("description") String description,
            @JsonProperty("accepted_delay") Integer accepted_delay,
            @JsonProperty("type") String type,
            @JsonProperty("category") String category,
            @JsonProperty("mode") String mode,
            @JsonProperty("carrier") ResultCarrier carrier
    ) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.accepted_delay = accepted_delay;
        this.type = type;
        this.category = category;
        this.carrier = carrier;
        this.mode = mode;
    }

}
