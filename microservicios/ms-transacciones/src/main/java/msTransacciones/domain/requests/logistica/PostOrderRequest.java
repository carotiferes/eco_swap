package msTransacciones.domain.requests.logistica;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.logistica.Item;
import msTransacciones.domain.logistica.ShipDirection;
import msTransacciones.domain.logistica.ShippingOption;
import msTransacciones.domain.logistica.enums.OrderStatusEnum;

import java.util.ArrayList;

@Builder
@Data
public class PostOrderRequest {

    private String comment;
    private ShipDirection ship_to;
    private ShipDirection ship_from;
    private ShippingOption shipping_option;
    private String store_id;
    private OrderStatusEnum status;
    private ArrayList<Item> items;
}
