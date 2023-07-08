package msUsers.exceptions.handler;

import msUsers.exceptions.responses.MethodArgumentNotValidExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("500 INTERNAL SERVER ERROR: " + exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotValidExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        MethodArgumentNotValidExceptionResponse response = new MethodArgumentNotValidExceptionResponse();
        List<String> errors = exception.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).toList();

        String errorMsg = String.join(" ", errors);

        response.setDescripcion(errorMsg);
        Date date = new Date();
        response.setTimestamp(date.getTime());
        response.setHttpStatus(HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(response);
    }
}
