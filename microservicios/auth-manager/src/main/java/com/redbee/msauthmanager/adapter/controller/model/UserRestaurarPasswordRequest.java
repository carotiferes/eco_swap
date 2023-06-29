package com.redbee.msauthmanager.adapter.controller.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Builder
@Data
public class UserRestaurarPasswordRequest {
    @NotBlank(message = "Nombre de usuario no debe estar vacio")
    @NotNull(message = "Nombre no debe estar vacio")
    String username;

    @NotBlank(message = "Contraseña no debe estar vacio")
    @NotNull(message = "Contraseña no debe estar vacio")
    @Size(min = 6, message = "Contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[ºª!|@·#$~%&¬/=?¡¿`^+*´¨çÇ,;.:\\-_<>]).{8,})",
            message = "Contraseña debe contener al menos 1 número, 1 caracteres especial, 1 mayúscula y 1 minúscula"
    )
    String password;

    @NotBlank(message = "Confirmar contraseña no debe estar vacio")
    String confirmPassword;

    @AssertTrue(message = "Las contraseñas no son iguales")
    private boolean isValid() {
        return password.equals(confirmPassword);
    }
}
