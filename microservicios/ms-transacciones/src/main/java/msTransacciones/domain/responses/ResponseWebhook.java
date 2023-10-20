package msTransacciones.domain.responses;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ResponseWebhook {
    private long idPayment;
    private String descripcion;
    private String status;
}
