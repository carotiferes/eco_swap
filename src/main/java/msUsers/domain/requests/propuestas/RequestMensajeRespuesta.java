package msUsers.domain.requests.propuestas;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.EstadoPropuesta;

import java.util.List;

@Data
@Builder
public class RequestMensajeRespuesta {

    private Long idEmisor;
    private String mensaje;
    private EstadoPropuesta estadoPropuesta;

    private List<ProductoSolicitudRequest> listadoPropuestas;

}
