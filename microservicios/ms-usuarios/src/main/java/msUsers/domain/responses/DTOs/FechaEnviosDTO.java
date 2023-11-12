package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.logistica.enums.EstadoEnvio;

@Data
public class FechaEnviosDTO {
    private long idOrden;
    private String fechaEnvio;
    private EstadoEnvio estado;
}
