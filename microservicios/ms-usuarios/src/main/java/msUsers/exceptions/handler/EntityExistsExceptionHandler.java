package msUsers.exceptions.handler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.exceptions.responses.EntityExistsExceptionResponse;
import msUsers.exceptions.responses.EntityNotFoundExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class EntityExistsExceptionHandler {
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<EntityExistsExceptionResponse> handle(EntityExistsException exception){
        EntityExistsExceptionResponse response = new EntityExistsExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.CONFLICT);
        log.warn(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
