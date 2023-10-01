package msTransacciones.exceptions.responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class EntityNotFoundExceptionResponse {

    private String descripcion;
    private long timestamp;
    private HttpStatus httpStatus;

}
