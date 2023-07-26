package msAutenticacion.domain.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import msAutenticacion.domain.requests.propuestas.RequestDireccion;
import msAutenticacion.domain.requests.propuestas.RequestSigninFundacion;
import msAutenticacion.domain.requests.propuestas.RequestSigninSwapper;

@Data
@Builder
public class RequestSignin {

    @NotNull(message = "username Debe existir")
    private String username;

    @NotNull(message = "email Debe existir")
    @Email
    private String email;

    @NotNull(message = "password Debe existir")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]){4,12}$",
            message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;

    @NotNull(message = "telefono Debe existir")
    private String telefono;

    @NotNull(message = "confirmPassword Debe existir")
    private String confirmPassword;

    //FUNDACION Y PARTICULAR PUEDEN SER NULL, SI EXISTE 1 NO PUEDE EXISTIR LA OTRA
    private RequestSigninFundacion fundacion;

    private RequestSigninSwapper particular;

    @NotNull
    private RequestDireccion direccion;
}
