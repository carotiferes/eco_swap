package msUsers.domain.logistica;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.logistica.enums.CheckInStatusEnum;

@Builder
@Data
public class CheckIn {
    private Long id;
    private String created_at;
    private String updated_at;
    private Long order_id;
    private Long warehouse_id;
    private CheckInStatusEnum status;
    private Boolean quality;
    private String scheduled_action;
    private String received_at;
    private String processing_at;
    private String completed_at;


}
