package com.redbee.msauthmanager.adapter.controller.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserLoginRequest {

    @NotBlank(message = "Nombre de usuario no debe estar vacio")
    @NotNull(message = "Nombre de usuario no debe estar vacio")
    private String username;

    @NotBlank(message = "Nombre de usuario no debe estar vacio")
    @NotNull(message = "Nombre de usuario no debe estar vacio")
    private String password;
}
