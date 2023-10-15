package msTransacciones.domain.model;

import lombok.Builder;
import lombok.Data;
import msTransacciones.domain.entities.Producto;
import msTransacciones.domain.logistica.Item;

@Builder
@Data
public class ItemAEnviar {

    String nombre;
    float peso;
    float altura;
    float ancho;
    float largo;
    Integer cantidad;

    public static ItemAEnviar crear(Producto producto, Integer cantidad) {
        return ItemAEnviar.builder()
                .ancho(1)
                .altura(1)
                .peso(1)
                .altura(1)
                .cantidad(cantidad)
                .nombre(producto.getDescripcion())
                .build();
    }
}
