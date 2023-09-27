package msAutenticacion.domain.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestPassword {

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]){4,12}$",
            message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String  nuevaPassword;

    @NotNull
    private String confirmarPassword;

    @AssertTrue(message = "Las contraseñas no coinciden.")
    private boolean isPasswordMatch() {
        return nuevaPassword != null && nuevaPassword.equals(confirmarPassword);
    }
}
