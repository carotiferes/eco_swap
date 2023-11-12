package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.requests.logistica.PostProductosRequest;
import msUsers.domain.responses.DTOs.FechaEnviosDTO;
import msUsers.domain.responses.DTOs.ProductosADonarDeOrdenDTO;

@Entity
@Data
@Builder
@Table(name = "ProductosADonar")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProducto")
@AllArgsConstructor
@NoArgsConstructor
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

    public ProductosADonarDeOrdenDTO toDTO() {
        ProductosADonarDeOrdenDTO productosADonarDeOrdenDTO = new ProductosADonarDeOrdenDTO();
        productosADonarDeOrdenDTO.setIdProductoADonar(idProductoADonar);
        productosADonarDeOrdenDTO.setIdOrden(ordenDeEnvio.getIdOrden());
        productosADonarDeOrdenDTO.setIdProducto(idProducto);
        productosADonarDeOrdenDTO.setIdDonacion(idDonacion);
        productosADonarDeOrdenDTO.setCantidad(cantidad);
        return productosADonarDeOrdenDTO;
    }
}
