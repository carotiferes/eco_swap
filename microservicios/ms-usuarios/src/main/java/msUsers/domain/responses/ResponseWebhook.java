package msUsers.domain.responses;

import lombok.Data;

@Data
public class ResponseWebhook {
    private long idPayment;
    private String descripcion;
    private String status;
}
