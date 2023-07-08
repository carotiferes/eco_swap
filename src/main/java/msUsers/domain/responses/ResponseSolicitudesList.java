package msUsers.domain.responses;

import lombok.Data;
import msUsers.domain.entities.Producto;

import java.util.List;

@Data
public class ResponseSolicitudesList {

    private long idSolicitud;
    private String fundacion;
    private String tituloSolicitud;
    private List<Producto> productos;
    private int cantidadDisponible;
    private String imagen;

}
