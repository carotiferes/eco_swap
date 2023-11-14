package msUsers.domain.responses.logistica.resultResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public ResultShippingInfo(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("operation_from_hour") String operation_from_hour,
            @JsonProperty("operation_to_hour") String operation_to_hour,
            @JsonProperty("drop_off_point") Boolean drop_off_point,
            @JsonProperty("pickup_point") Boolean pickup_point,
            @JsonProperty("type") String type
    ) {
        this.id = id;
        this.name = name;
        this.operation_from_hour = operation_from_hour;
        this.operation_to_hour = operation_to_hour;
        this.drop_off_point = drop_off_point;
        this.pickup_point = pickup_point;
        this.type = type;
    }

}
