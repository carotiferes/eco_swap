package msUsers.domain.requests;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.EstadoPropuesta;

@Data
@Builder
public class RequestMensajeRespuesta {

    private Long idEmisor;
    private String mensaje;
    private EstadoPropuesta estadoPropuesta;

}
