package msTransacciones.domain.requests.donaciones;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.entities.enums.EstadoDonacion;

@Data
@Builder
public class RequestMensajeRespuesta {

    private Long idEmisor;
    private String mensaje;
    private EstadoDonacion estadoDonacion;

}
