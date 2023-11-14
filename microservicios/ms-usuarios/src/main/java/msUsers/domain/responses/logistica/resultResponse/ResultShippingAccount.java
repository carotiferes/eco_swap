package msUsers.domain.responses.logistica.resultResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultShippingAccount {
    private Long id;

    @JsonCreator
    public ResultShippingAccount(@JsonProperty("id") Long id) {
        this.id = id;
    }
}
