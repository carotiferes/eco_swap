package msTransacciones.domain.client.shipnow.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseOrders {

    Boolean archived;
    String comment;
    String created_at;
    String dispatched_for;

}
