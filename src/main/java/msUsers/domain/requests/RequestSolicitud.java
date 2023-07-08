package msUsers.domain.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RequestSolicitud {

    @NotNull(message = "La solicitud debe contener un título.")
    @Size(max = 50)
    private String titulo;

    @NotNull(message = "La solicitud debe contener una descripción.")
    private String descripcion;

    private long idFundacion;

    @NotNull(message = "La solicitud debe tener una lista de productos.")
    private List<RequestProducto> productos;

    private String imagen;
}
