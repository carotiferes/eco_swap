package msUsers.domain.requests.propuestas;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RequestComunicarPropuestaSolicitudModel {

    @NotNull(message = "idSwapper Debe existir un ID Pefil")
    private Long idSwapper;

    @NotNull(message = "solicitudProductoModel Debe existir")
    private SolicitudProductoRequest solicitudProductoModel;


}
