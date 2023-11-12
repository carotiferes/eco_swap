package msUsers.domain.responses.DTOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.logistica.enums.OrdenEstadoEnum;
import msUsers.domain.responses.logisticaResponse.ResponseFechasEnvio;

@Data
public class FechaEnviosDTO {
    private long idOrden;
    private String fechaEnvio;
    private OrdenEstadoEnum estado;
}
