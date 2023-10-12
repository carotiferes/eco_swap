package msTransacciones.domain.client.shipnow;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.logistica.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Builder
@Data
public class ResponseOrder {

    Long id;
    String last_updated_date;
    String external_reference;
    String external_reference_user;
    String comment;
    String status;
    Timestamps timestamps;
    String estimate_delivery;
    ArrayList<Item> items;
}
