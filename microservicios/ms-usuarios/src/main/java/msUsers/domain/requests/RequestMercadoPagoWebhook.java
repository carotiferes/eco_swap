package msUsers.domain.requests;

import lombok.Data;
import msUsers.domain.model.DataRequestMP;

@Data
public class RequestMercadoPagoWebhook {
    private Long id;
    private Boolean live_mode;
    private String type;
    private String date_created;
    private Long userId;
    private String api_version;
    private String action;
    private DataRequestMP data;
}
