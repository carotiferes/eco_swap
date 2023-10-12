package msTransacciones.domain.responses.logistica.resultResponse;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ResultShippingOptions {

    private String minimum_delivery;
    private String maximum_delivery;

    private Float price;
    private Float tax_price;
    private String ship_from_type;

    //OBJECT
    private ResultShippingInfo ship_from;

    private String ship_to_type;
    private String ship_to;
    private ResultShippingContract shipping_contract;
    private ResultShippingService shipping_service;
}
