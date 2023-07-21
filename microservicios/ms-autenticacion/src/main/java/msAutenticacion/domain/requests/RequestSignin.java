package msAutenticacion.domain.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msAutenticacion.domain.requests.propuestas.RequestDireccion;
import msAutenticacion.domain.requests.propuestas.RequestSigninFundacion;
import msAutenticacion.domain.requests.propuestas.RequestSigninSwapper;

@Data
@Builder
public class RequestSignin {

    @NotNull(message = "solicitudProductoModel Debe existir")
    private String username;

    @NotNull(message = "solicitudProductoModel Debe existir")
    @Email
    private String email;

    @NotNull(message = "solicitudProductoModel Debe existir")
    private String password;

    @NotNull
    private String telefono;

    @NotNull(message = "solicitudProductoModel Debe existir")
    private String confirmPassword;

    //FUNDACION Y PARTICULAR PUEDEN SER NULL, SI EXISTE 1 NO PUEDE EXISTIR LA OTRA
    private RequestSigninFundacion fundacion;

    private RequestSigninSwapper particular;

    @NotNull
    private RequestDireccion direccion;
}
