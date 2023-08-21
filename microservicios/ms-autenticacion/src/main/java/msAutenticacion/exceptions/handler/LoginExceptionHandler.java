package msAutenticacion.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.exceptions.LoginUserBlockedException;
import msAutenticacion.exceptions.LoginUserException;
import msAutenticacion.exceptions.responses.EntityNotFoundExceptionResponse;
import msAutenticacion.exceptions.responses.UserBlockedExceptionResponse;
import msAutenticacion.exceptions.LoginUserWrongCredentialsException;
import msAutenticacion.exceptions.responses.WrongCredentialsExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
@Order(1)
public class LoginExceptionHandler {
    @ExceptionHandler(LoginUserException.class)
    public ResponseEntity<EntityNotFoundExceptionResponse> handleLoginException(LoginUserException exception){
        EntityNotFoundExceptionResponse response = new EntityNotFoundExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        log.warn(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(LoginUserWrongCredentialsException.class)
    public ResponseEntity<WrongCredentialsExceptionResponse> handleWrongCredentialsException(LoginUserWrongCredentialsException exception){
        WrongCredentialsExceptionResponse response = new WrongCredentialsExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.UNAUTHORIZED);
        log.warn(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(LoginUserBlockedException.class)
    public ResponseEntity<UserBlockedExceptionResponse> handleLoginBlockedException(LoginUserBlockedException exception){
        UserBlockedExceptionResponse response = new UserBlockedExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.FORBIDDEN);
        log.warn(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
