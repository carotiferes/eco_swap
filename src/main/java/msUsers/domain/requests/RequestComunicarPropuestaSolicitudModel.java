package msUsers.domain.requests;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.model.SolicitudProductoModel;

@Data
@Builder
public class RequestComunicarPropuestaSolicitudModel {
    private Long idPerfilEmisor;
    private Long idSolicitud;
    private SolicitudProductoModel solicitudProductoModel;

}
