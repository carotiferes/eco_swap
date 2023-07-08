package msUsers.domain.requests.propuestas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestComunicarPropuestaSolicitudModel {
    @NotNull(message = "idPerfilEmisor Debe existir un ID Pefil")
    private Long idPerfilEmisor;

    @NotNull(message = "solicitudProductoModel Debe existir")
    private SolicitudProductoRequest solicitudProductoModel;

    private List<ProductoSolicitudRequest> listadoProductos;

}
