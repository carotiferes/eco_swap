package msAutenticacion.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.exceptions.responses.EntityNotFoundExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
@Order(2)
public class EntityNotFoundExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityNotFoundExceptionResponse> handle(EntityNotFoundException exception){
        EntityNotFoundExceptionResponse response = new EntityNotFoundExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        log.warn(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
