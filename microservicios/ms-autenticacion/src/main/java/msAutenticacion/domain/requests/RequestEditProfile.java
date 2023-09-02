package msAutenticacion.domain.requests;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import msAutenticacion.domain.requests.propuestas.RequestDireccion;
import msAutenticacion.domain.requests.propuestas.RequestSignUpFundacion;
import msAutenticacion.domain.requests.propuestas.RequestSignUpSwapper;

@Data
public class RequestEditProfile {

    @NotNull(message = "telefono Debe existir")
    private String telefono;

    //FUNDACION Y PARTICULAR PUEDEN SER NULL, SI EXISTE 1 NO PUEDE EXISTIR LA OTRA
    private RequestSignUpFundacion fundacion;

    private RequestSignUpSwapper particular;

    @NotNull
    private RequestDireccion direccion;
}
