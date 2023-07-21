package msUsers.domain.requests.donaciones;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RequestComunicarDonacionColectaModel {

    @NotNull(message = "idParticular Debe existir un ID Pefil")
    private Long idParticular;

    @NotNull(message = "colectaProductoModel Debe existir")
    private ColectaProductoRequest colectaProductoModel;


}
