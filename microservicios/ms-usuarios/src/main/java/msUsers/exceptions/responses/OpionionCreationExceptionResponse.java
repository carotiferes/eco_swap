package msUsers.exceptions.responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class OpionionCreationExceptionResponse {
    private String descripcion;
    private long timestamp;
    private HttpStatus httpStatus;
}
