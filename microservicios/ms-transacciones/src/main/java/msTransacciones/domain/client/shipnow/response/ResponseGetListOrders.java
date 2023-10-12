package msTransacciones.domain.client.shipnow.response;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.logistica.Order;

import java.util.ArrayList;

@Builder
@Data
public class ResponseGetListOrders {

    public ArrayList<Order> results;
}
