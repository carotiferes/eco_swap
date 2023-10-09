package msTransacciones.domain.logistica.enums;

public enum OrderStatusEnum {
    AWAITING_PAYMENT("awaiting_payment"),
    ON_HOLD("on_hold"),
    CANCELLED("cancelled"),
    AWAITING_SHIPMENT("awaiting_shipment"),
    FILTERED("filtered"),
    SHIPPER("shipper"),
    NOT_DELIVERED("not_delivered"),
    DELIVERED("delivered"),
    RETURN("return"),
    READY_TO_PICK("ready_to_pick");

    public final String name;

    private OrderStatusEnum(String name) {
        this.name = name;
    }
}
