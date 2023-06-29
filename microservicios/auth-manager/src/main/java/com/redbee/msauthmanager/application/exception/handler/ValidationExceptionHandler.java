package com.redbee.msauthmanager.application.exception.handler;

import com.redbee.msauthmanager.domain.ErrorValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

    /*
        Se activa cada vez que en el request se detecta una falla en la validación de los datos de entrada
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValidationResponse> handle(MethodArgumentNotValidException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorValidationResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .mensaje(ErrorValidationResponse.obtenerRespuestasDeErrores(ex.getAllErrors()))
                        .code(100)
                        .build());
    }
}

