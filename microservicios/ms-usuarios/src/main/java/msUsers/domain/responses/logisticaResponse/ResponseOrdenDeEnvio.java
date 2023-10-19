package msUsers.domain.responses.logisticaResponse;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ResponseOrdenDeEnvio {

    private String orderId;
    private String usernameDestino;
    private List<ResponseFechasEnvio> listaFechaEstado;
}
