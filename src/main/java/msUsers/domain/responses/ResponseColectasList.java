package msUsers.domain.responses;

import lombok.Data;
import msUsers.domain.entities.Producto;

import java.util.List;

@Data
public class ResponseColectasList {

    private long idColecta;
    private String fundacion;
    private long idFundacion;
    private String tituloColecta;
    private List<Producto> productos;
    private int cantidadDisponible;
    private String imagen;

}
