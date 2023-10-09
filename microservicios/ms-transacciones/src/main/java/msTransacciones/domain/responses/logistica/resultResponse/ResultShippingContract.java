package msTransacciones.domain.responses.logistica.resultResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultShippingContract {
    private Long id;
    private ResultShippingAccount account;
    private ResultShippingServiceId shipping_service;
}
