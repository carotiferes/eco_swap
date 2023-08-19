package msUsers.domain.responses.DTOs;


import lombok.Data;
import msUsers.domain.entities.CaracteristicaDonacion;
import msUsers.domain.entities.enums.EstadoDonacion;

import java.util.List;

@Data
public class DonacionDTO {
    private String descripcion;
    private int cantidadDonacion;
    private EstadoDonacion estadoDonacion;
    private String nombreParticular;
    private long idParticular;
    private ProductoDTO producto;
    private List<CaracteristicaDonacion> caracteristicaDonacion;
    private String imagenes;
}
