package msUsers.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import msUsers.exceptions.DonacionCreationException;
import msUsers.exceptions.OpinionCreationException;
import msUsers.exceptions.responses.DonationCreationExceptionResponse;
import msUsers.exceptions.responses.OpionionCreationExceptionResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@Slf4j
@ControllerAdvice
@Order(2)
public class OpinionExceptionsHandler {

    @ExceptionHandler(OpinionCreationException.class)
    public ResponseEntity<OpionionCreationExceptionResponse> handle(OpinionCreationException exception){
        OpionionCreationExceptionResponse response = new OpionionCreationExceptionResponse();
        response.setDescripcion(exception.getMessage());
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.CONFLICT);
        log.warn(">> {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
