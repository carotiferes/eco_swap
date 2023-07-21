package msUsers.domain.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RequestColecta {

    @NotBlank(message = "La colecta debe contener un título.")
    @Size(max = 50)
    private String titulo;

    @NotBlank(message = "La colecta debe contener una descripción.")
    private String descripcion;

    private long idFundacion;

    @NotNull(message = "La colecta debe tener una lista de productos.")
    @Valid
    private List<RequestProducto> productos;

    private String imagen;

}
