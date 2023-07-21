package msAutenticacion.domain.responses;

import lombok.Data;
import msAutenticacion.domain.entities.Producto;

import java.util.List;

@Data
public class ResponseSolicitudesList {

    private long idSolicitud;
    private String fundacion;
    private long idFundacion;
    private String tituloSolicitud;
    private List<Producto> productos;
    private int cantidadDisponible;
    private String imagen;

}
