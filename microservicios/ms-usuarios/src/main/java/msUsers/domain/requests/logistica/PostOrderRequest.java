package msUsers.domain.requests.logistica;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class PostOrderRequest {

    private String titulo;
    private Long userIdDestino;
    private Long userIdOrigen;
    private Long idPublicacion;
    private Long idColecta;
    private List<PostProductosRequest> listProductos;
    private float costoEnvio;

}
