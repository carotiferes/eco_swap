package msUsers.domain.responses.logistica.resultResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultShippingContract {
    private Long id;
    private ResultShippingAccount account;
    private ResultShippingServiceId shipping_service;
    private String status;

    @JsonCreator
    public ResultShippingContract(
            @JsonProperty("id") Long id,
            @JsonProperty("account") ResultShippingAccount account,
            @JsonProperty("shipping_service") ResultShippingServiceId shipping_service,
            @JsonProperty("status") String status
    ) {
        this.id = id;
        this.account = account;
        this.shipping_service = shipping_service;
        this.status = status;
    }
}
