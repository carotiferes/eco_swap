package msAutenticacion.domain.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestEditAvatar {

    @NotNull(message = "No debe ser null el nuevo avatar.")
    @NotBlank(message = "No puede estar en blanco el parámetro 'avatar'.")
    private String avatar;
}
