package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.requests.logistica.PostProductosRequest;

@Entity
@Data
@Builder
@Table(name = "ProductosADonar")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProducto")
public class ProductosADonarDeOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idProductoADonar;
    @NotNull
    private long idProducto;
    @NotNull
    private long cantidad;
    @ManyToOne(cascade = CascadeType.ALL)
    private OrdenDeEnvio ordenDeEnvio;

    public static ProductosADonarDeOrden crearProductoADonar(Producto producto) {
        return ProductosADonarDeOrden.builder()
                .idProducto(producto.getIdProducto())
                .build();
    }

    public static ProductosADonarDeOrden crearProductoADonarRequest(PostProductosRequest producto) {
        return ProductosADonarDeOrden.builder()
                .idProducto(producto.getProductoId())
                .cantidad(producto.getCantidad())
                .build();
    }
}
