package msAutenticacion.domain.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import msAutenticacion.domain.requests.propuestas.RequestDireccion;
import msAutenticacion.domain.requests.propuestas.RequestSignUpFundacion;
import msAutenticacion.domain.requests.propuestas.RequestSignUpSwapper;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
public class RequestSignUp {

    @NotNull(message = "username Debe existir")
    private String username;

    @NotNull(message = "email Debe existir")
    @Email
    private String email;

    @NotNull(message = "password Debe existir")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{4,30}$",
            message = "La contraseña debe tener al menos 4 caracteres y como máximo 30 conteniendo una mayúscula, una minúscula, un caracter especial y al menos un digito.")
    private String password;

    @NotNull(message = "telefono Debe existir")
    private String telefono;

    @NotNull(message = "confirmPassword Debe existir")
    private String confirmPassword;

    //FUNDACION Y PARTICULAR PUEDEN SER NULL, SI EXISTE 1 NO PUEDE EXISTIR LA OTRA
    private RequestSignUpFundacion fundacion;

    private RequestSignUpSwapper particular;

    @NotNull
    private RequestDireccion direccion;

    @AssertTrue(message = "Contraseña y confirmar contraseña no son iguales")
    public boolean contraseniasIguales() {
        return password.equals(confirmPassword);
    }
}
