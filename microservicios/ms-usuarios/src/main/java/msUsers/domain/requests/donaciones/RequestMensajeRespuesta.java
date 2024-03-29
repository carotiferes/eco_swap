package msUsers.domain.requests.donaciones;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.EstadoDonacion;

@Data
@Builder
public class RequestMensajeRespuesta {

    private Long idEmisor;
    private String mensaje;
    private EstadoDonacion estadoDonacion;

}
