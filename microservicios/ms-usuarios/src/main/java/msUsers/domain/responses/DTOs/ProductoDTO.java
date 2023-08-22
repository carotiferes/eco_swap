package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.entities.Colecta;
import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.Producto;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.enums.TipoProducto;

import java.util.List;

@Data
public class ProductoDTO {

    private TipoProducto tipoProducto;
    private ColectaDTO colectaDTO;
    private String descripcion;
    private int cantidadSolicitada;
    private int cantidadRecibida;
//    private List<DonacionDTO> donaciones;
    private PublicacionDTO publicacion;

}
