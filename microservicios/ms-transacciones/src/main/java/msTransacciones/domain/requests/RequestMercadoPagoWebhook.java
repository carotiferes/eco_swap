package msTransacciones.domain.requests;

import lombok.Data;
import msTransacciones.domain.model.DataRequestMP;

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
