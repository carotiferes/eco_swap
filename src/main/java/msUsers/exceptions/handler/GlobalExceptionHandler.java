package msUsers.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("500 INTERNAL SERVER ERROR: " + exception.getMessage());
    }
}
