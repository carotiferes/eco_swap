package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;


@Data
public class ProductoDTO {

    private Long idProducto;
    private TipoProducto tipoProducto;
    private ColectaDTO colectaDTO;
    private String descripcion;
    private int cantidadSolicitada;
    private int cantidadRecibida;
    private int cantidadEnCamino;
}
