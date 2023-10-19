package msUsers.domain.requests.logistica;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Builder
@Data
public class PostOrderRequest {

    private String titulo;
    private Long userIdDestino;
    private Long userIdOrigen;
    private Long publicacionOColectaId;
    private Boolean esPublicacion;
    private Integer cantidad;
    private ArrayList<Long> idDeItems;
    private float costoEnvio;

}
