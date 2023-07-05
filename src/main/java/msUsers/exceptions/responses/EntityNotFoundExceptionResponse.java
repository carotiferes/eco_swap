package msUsers.exceptions.responses;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;

public class EntityNotFoundExceptionResponse {

    private String descripcion;
    private long timestamp;
    private HttpStatus httpStatus;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
