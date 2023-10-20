package msUsers.domain.logistica;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Timestamps {

    private String created_at;
    private String updated_at;
    private String ready_to_pick_at;
    private String packing_slip_at;
    private String picking_list_at;
    private String ready_to_ship_at;
    private String shipped_at;
    private String delivered_at;
    private String not_delivered_at;
    private String on_hold_at;
}
