package msUsers.domain.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestPuedeOpinar {
    @NotNull(message = "Se necesita el id del usuario que desea emitir la opinión.")
    private Long idUsuarioOpinador;
    @NotNull(message = "Se necesita el id del usuario al que desea dejarle su opinión.")
    private Long idUsuarioOpinado;
}
