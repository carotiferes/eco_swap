package msTransacciones.domain.model;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.entities.Direccion;

@Builder
@Data
public class DirrecionAEnviar {

    private Long idDireccion;
    private String nombreDireccion;
    private String altura;
    private String piso;
    private String dpto;
    private String codigoPostal;

    public static DirrecionAEnviar crear(Direccion direccion) {
        return DirrecionAEnviar.builder()
                .altura(direccion.getAltura())
                .nombreDireccion(direccion.getDireccion())
                .dpto(direccion.getDpto())
                .codigoPostal(direccion.getCodigoPostal())
                .piso(direccion.getPiso())
                .build();
    }
}
