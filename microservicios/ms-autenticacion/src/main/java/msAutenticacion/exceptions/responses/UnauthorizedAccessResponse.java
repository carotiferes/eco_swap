package msAutenticacion.exceptions.responses;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UnauthorizedAccessResponse {
    private String descripcion;
    private long timestamp;
    private HttpStatus httpStatus;

    public UnauthorizedAccessResponse(String descripcion, long timestamp, HttpStatus httpStatus) {
        this.descripcion = descripcion;
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
    }
}
