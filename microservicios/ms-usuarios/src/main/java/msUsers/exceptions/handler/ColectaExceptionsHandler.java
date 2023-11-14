package msUsers.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import msUsers.exceptions.DonacionCreationException;
import msUsers.exceptions.responses.DonationCreationExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
@Order(3)
public class ColectaExceptionsHandler {
    @ExceptionHandler(DonacionCreationException.class)
    public ResponseEntity<DonationCreationExceptionResponse> handle(DonacionCreationException exception){
        DonationCreationExceptionResponse response = new DonationCreationExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        log.warn(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
