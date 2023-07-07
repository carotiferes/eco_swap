package msUsers.domain.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.model.SolicitudProductoModel;

@Data
@Builder
public class RequestComunicarPropuestaSolicitudModel {
    @NotNull
    private Long idPerfilEmisor;

    @NotNull
    private SolicitudProductoModel solicitudProductoModel;

}
