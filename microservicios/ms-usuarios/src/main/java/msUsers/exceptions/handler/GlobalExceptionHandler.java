package msUsers.exceptions.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import msUsers.exceptions.responses.MethodArgumentNotValidExceptionResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;

@Slf4j
@ControllerAdvice
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception){
        log.error("500 INTERNAL SERVER ERROR: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("500 INTERNAL SERVER ERROR: " + exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        log.error("ERROR - Problemas al recibir la request: {}", exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getLocalizedMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
                                                                           HttpServletRequest request) {
        String msg = "ERROR - La URL " + request.getRequestURL().toString() + "no soporta " + exception.getMethod();
        log.error("ERROR - La URL: {} no soporta {}", request.getRequestURL().toString(), exception.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(msg);
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
        log.warn(">> BAD REQUEST: {}", errorMsg);
        return ResponseEntity.badRequest().body(response);
    }
}
