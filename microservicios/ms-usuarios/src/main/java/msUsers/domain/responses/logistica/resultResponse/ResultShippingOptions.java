package msUsers.domain.responses.logistica.resultResponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @JsonCreator
    public ResultShippingOptions(
            @JsonProperty("minimum_delivery") String minimum_delivery,
            @JsonProperty("maximum_delivery") String maximum_delivery,
            @JsonProperty("price") Float price,
            @JsonProperty("tax_price") Float tax_price,
            @JsonProperty("ship_from_type") String ship_from_type,
            @JsonProperty("ship_from") ResultShippingInfo ship_from,
            @JsonProperty("ship_to_type") String ship_to_type,
            @JsonProperty("ship_to") String ship_to,
            @JsonProperty("shipping_contract") ResultShippingContract shipping_contract,
            @JsonProperty("shipping_service") ResultShippingService shipping_service
    ) {
        this.minimum_delivery = minimum_delivery;
        this.maximum_delivery = maximum_delivery;
        this.price = price;
        this.tax_price = tax_price;
        this.ship_from_type = ship_from_type;
        this.ship_from = ship_from;
        this.ship_to_type = ship_to_type;
        this.ship_to = ship_to;
        this.shipping_contract = shipping_contract;
        this.shipping_service = shipping_service;
    }
}
