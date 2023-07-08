package msUsers.domain.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.model.SolicitudProductoModel;

@Data
@Builder
public class RequestComunicarPropuestaSolicitudModel {

    @NotNull(message = "idPerfilEmisor Debe existir un ID Pefil")
    private Long idPerfilEmisor;

    @NotNull(message = "solicitudProductoModel Debe existir")
    private SolicitudProductoModel solicitudProductoModel;

}
