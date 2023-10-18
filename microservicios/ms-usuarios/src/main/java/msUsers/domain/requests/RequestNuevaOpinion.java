package msUsers.domain.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestNuevaOpinion {

    @NotNull(message = "Se necesita el usuario por el cual se va a opinar.")
    private long idUsuarioOpinado;

    @NotNull(message = "Se necesita una valoración para opinar.")
    @Max(value = 5, message = "La valoración máxima permitida es de 5 estrellas.")
    @Min(value = 0, message = "La valoración mínima permitida es de 0 estrellas.")
    private float valoracion;

    @NotNull(message = "Es necesaria una opinión escrita.")
    @NotBlank(message = "La descripción de la opinión no puede estar vacía.")
    private String descripcion;
}
