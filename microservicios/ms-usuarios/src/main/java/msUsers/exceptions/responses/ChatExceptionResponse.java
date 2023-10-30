package msUsers.exceptions.responses;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ChatExceptionResponse {
    private String descripcion;
    private long timestamp;
    private HttpStatus httpStatus;
}
