package msTransacciones.domain.logistica;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.logistica.enums.OrderStatusEnum;

import java.math.BigDecimal;
import java.util.ArrayList;

@Builder
@Data
public class Order {

    private Long id;
    private String created_at;
    private String update_at;
    private String external_reference;
    private String external_reference_user;
    private String comment;
    private OrderStatusEnum status;
    private OrderStatusEnum last_status;
    private Long main_order_id;
    private Boolean archived;
    private Timestamps timestamps;
    private BigDecimal weight;
    private String uid;
    private String package_type;
    private String estimate_delivery;
    private String minimum_delivery;
    private Long ship_from_warehouse_id;
    private Long ship_to_warehouse_id;
    private String type;
    private String seller_id;
    private String sellet_name;
    private String seller_logo;
    private String store_type;
    private Boolean cross_docking;
    private Payment payment;
    private ShippingOption shipping_option;
    private Store store;
    private CheckIn check_in;
    private ShipDirection ship_to;
    private ShipDirection ship_from;
    private ArrayList<Item> items;
}
