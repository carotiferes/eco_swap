package msUsers.domain.responses.logistica.resultResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String image_url;
    @JsonCreator
    public ResultCarrier(
            @JsonProperty("code") String code,
            @JsonProperty("description") String description,
            @JsonProperty("flexible_dispatching") Boolean flexible_dispatching,
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("tracking_url") String tracking_url,
            @JsonProperty("image_url") String image_url
    ) {
        this.code = code;
        this.description = description;
        this.flexible_dispatching = flexible_dispatching;
        this.id = id;
        this.name = name;
        this.tracking_url = tracking_url;
        this.image_url = image_url;
    }

}
