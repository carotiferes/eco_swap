package msAutenticacion.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.exceptions.PasswordUpdateException;
import msAutenticacion.exceptions.UserCreationException;
import msAutenticacion.exceptions.UserDuplicatedException;
import msAutenticacion.exceptions.ValidationUserException;
import msAutenticacion.exceptions.responses.PasswordUpdateExceptionResponse;
import msAutenticacion.exceptions.responses.UserCreationExceptionResponse;
import msAutenticacion.exceptions.responses.UserDuplicatedExceptionResponse;
import msAutenticacion.exceptions.responses.ValidationUserExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
@Order(3)
public class UserExceptionHandler {
    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<UserCreationExceptionResponse> handleUserCreationException(UserCreationException exception){
        UserCreationExceptionResponse response = new UserCreationExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity<UserDuplicatedExceptionResponse> handleUserCreationException(UserDuplicatedException exception){
        UserDuplicatedExceptionResponse response = new UserDuplicatedExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.CONFLICT);
        log.error(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ValidationUserException.class)
    public ResponseEntity<ValidationUserExceptionResponse> handleValidationException(ValidationUserException exception){
        ValidationUserExceptionResponse response = new ValidationUserExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        log.error(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PasswordUpdateException.class)
    public ResponseEntity<PasswordUpdateExceptionResponse> handlePasswordUpdateException(PasswordUpdateException exception){
        PasswordUpdateExceptionResponse response = new PasswordUpdateExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        log.error(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UserCreationExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        UserCreationExceptionResponse response = new UserCreationExceptionResponse();
        response.setDescripcion("Error. El registro que intentas crear ya existe en la base de datos.");
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.CONFLICT);
        log.error(">> ERROR. El registro que intentas crear ya existe en la base de datos: {} ", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
