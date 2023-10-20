package msUsers.domain.responses.logisticaResponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseCostoEnvio {

    private String fechaMaximaEnvio;
    private Float precio;
}
