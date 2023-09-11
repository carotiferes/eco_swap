package msUsers.domain.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestTrueque {

    @NotNull(message = "Debe especificarse una publicación de origen.")
    private Long idPublicacionOrigen;
    @NotNull(message = "Debe especificarse una publicación de propuesta.")
    private Long idPublicacionPropuesta;
}
