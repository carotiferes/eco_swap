package msTransacciones.domain.model;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.entities.Direccion;

import java.util.List;

@Builder
@Data
public class OrderAEnviar {

    String titulo;
    String nombreUserDestino;
    String nombreUserOrigen;
    DirrecionAEnviar dirrecionAEnviarOrigen;
    DirrecionAEnviar direccionAEnviarDestino;
    List<ItemAEnviar> listItemAEnviar;
}
