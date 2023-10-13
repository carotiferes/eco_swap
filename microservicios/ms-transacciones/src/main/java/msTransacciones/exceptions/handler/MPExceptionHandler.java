package msTransacciones.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(1)
public class MPExceptionHandler {
    @ExceptionHandler(MPException.class)
    public ResponseEntity<String> handleException(MPException exception){
        log.error("Error con MercadoPago: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error con MercadoPago: " + exception.getMessage());
    }
    @ExceptionHandler(MPApiException.class)
    public ResponseEntity<String> handleException(MPApiException exception){
        log.error("Error con la API de MercadoPago: {}", exception.getApiResponse().getContent());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error con la API de MercadoPago: " + exception.getApiResponse().getContent());
    }
}
