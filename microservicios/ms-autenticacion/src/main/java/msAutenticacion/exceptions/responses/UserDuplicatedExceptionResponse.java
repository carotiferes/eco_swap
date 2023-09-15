package msAutenticacion.exceptions.responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UserDuplicatedExceptionResponse {
    private String descripcion;
    private long timestamp;
    private HttpStatus httpStatus;
}
