package com.redbee.msauthmanager.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    private final HttpServletRequest httpServletRequest;

    public ErrorHandler(final HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handle(Throwable ex) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return buildResponseError(HttpStatus.INTERNAL_SERVER_ERROR,ex, ErrorCode.INTERNAL_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handle(MissingServletRequestParameterException ex) {
        log.error(ErrorCode.INVALID_PARAMETERS_ERROR.getCode(), ex);
        return buildResponseError(HttpStatus.BAD_REQUEST,ex, ErrorCode.INVALID_PARAMETERS_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildCustomResponseError(HttpStatus httpStatus, ErrorCode errorCode, String customDescription) {
        List<String> mensajes = new ArrayList<>();
        mensajes.add(customDescription);
        ErrorResponse response = ErrorResponse.builder()
                .mensaje(mensajes)
                .code(errorCode.getValue())
                .build();

        return new ResponseEntity<>(response, httpStatus);
    }

    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ErrorResponse {

        private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSSSSS]['Z']";

        @JsonProperty
        private Integer status;
        @JsonProperty
        private Integer code;
        @JsonProperty
        private List<String> mensaje;
    }


    private ResponseEntity<ErrorResponse> buildResponseError(HttpStatus httpStatus, Throwable ex, ErrorCode errorCode) {
/*
        String queryString = Optional.ofNullable(httpServletRequest.getQueryString())
                .orElse("");

        Map<String, String> metaData = new HashMap<String, String>();
        metaData.entrySet("query_string");
*/

        List<String> listadoMensaje = new ArrayList<>();
        listadoMensaje.add(ex.getMessage());

        final ErrorResponse apiErrorResponse = ErrorResponse
                .builder()
                .mensaje(listadoMensaje)
                .status(httpStatus.value())
                .code(errorCode.getValue())
                .build();

        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

}

