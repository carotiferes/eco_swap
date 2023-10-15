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

    private String titulo;
    private Long userIdDestino;
    private Long userIdOrigen;
    private Integer cantidad;
    private ArrayList<Long> idDeItems;
    private float costoEnvio;

}
