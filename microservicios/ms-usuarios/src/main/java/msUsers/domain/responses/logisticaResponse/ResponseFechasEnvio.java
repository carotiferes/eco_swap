package msUsers.domain.responses.logisticaResponse;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseFechasEnvio {

    private String fecha;
    private String estado;
}
