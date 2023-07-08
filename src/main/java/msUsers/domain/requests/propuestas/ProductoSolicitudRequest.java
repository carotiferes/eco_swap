package msUsers.domain.requests.propuestas;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.PropuestaProductos;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Builder
@Data
public class ProductoSolicitudRequest {


    private Long id;

    private Long idProducto;

    private Integer cantSolicitada;

    public PropuestaProductos toDomain() {
        return id!=null ?
                PropuestaProductos.builder()
                        .id(id)
                        .cantidad(cantSolicitada)
                        .idProductoQueHaceReferencia(idProducto)
                        .build() :
                PropuestaProductos.builder()
                        .cantidad(cantSolicitada)
                        .idProductoQueHaceReferencia(idProducto)
                        .build();
    }
}
