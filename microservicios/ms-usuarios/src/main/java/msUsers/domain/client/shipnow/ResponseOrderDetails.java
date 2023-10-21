package msUsers.domain.client.shipnow;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.logistica.*;

import java.util.ArrayList;

@Builder
@Data
public class ResponseOrderDetails {

    Long id;
    String created_at;
    String last_updated_date;
    String external_reference;
    String external_reference_user;
    String comment;
    String status;
    Timestamps timestamps;
    String estimate_delivery;
    ShippingOption shippingOption;
    Store store;
    ShipDirection ship_to;
    ShipDirection ship_from;
    ArrayList<Item> items;
}
