package msUsers.domain.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestMensajeRespuesta {

    private Long idEmisor;
    private Long idComunicacion;
    private String mensaje;

}
