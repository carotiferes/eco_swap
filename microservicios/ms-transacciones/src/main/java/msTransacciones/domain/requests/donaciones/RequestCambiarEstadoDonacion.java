package msTransacciones.domain.requests.donaciones;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCambiarEstadoDonacion {

    @NotNull(message = "Es necesario un estado para hacer la actualización de la donación")
    private String nuevoEstado;
}
