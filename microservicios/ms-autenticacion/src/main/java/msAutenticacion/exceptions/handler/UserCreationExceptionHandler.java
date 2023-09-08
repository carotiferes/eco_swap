package msAutenticacion.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.exceptions.UserCreationException;
import msAutenticacion.exceptions.responses.UserCreationExceptionResponse;
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
public class UserCreationExceptionHandler {
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
