package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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
    @ToString.Exclude
    private OrdenDeEnvio ordenDeEnvio;
    @NotNull
    private Long idDonacion;

    public static ProductosADonarDeOrden crearProductoADonar(Producto producto) {
        return ProductosADonarDeOrden.builder()
                .idProducto(producto.getIdProducto())
                .build();
    }

    public static ProductosADonarDeOrden crearProductoADonarRequest(PostProductosRequest producto) {
        return ProductosADonarDeOrden.builder()
                .idProducto(producto.getProductoId())
                .cantidad(producto.getCantidad())
                .idDonacion(producto.getDonacionId())
                .build();
    }
}
