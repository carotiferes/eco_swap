package com.redbee.msauthmanager.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class ErrorValidationResponse {

    private int status;
    private List<String> mensaje;
    private int code;

    public static List<String> obtenerRespuestasDeErrores(List<ObjectError> errores) {
        return errores.stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
