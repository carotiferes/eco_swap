package msTransacciones.domain.requests.trueques;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCambiarEstadoTrueque {

    @NotNull(message = "Es necesario un estado para hacer la actualización del trueque")
    private String nuevoEstado;
}
