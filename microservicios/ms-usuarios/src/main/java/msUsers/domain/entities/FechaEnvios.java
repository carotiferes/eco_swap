package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.logistica.enums.OrdenEstadoEnum;
import msUsers.domain.responses.logisticaResponse.EnumEstadoOrden;
import msUsers.domain.responses.logisticaResponse.ResponseFechasEnvio;

@Entity
@Table(name = "FechaEnvios")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FechaEnvios {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idOrden;
    @NotNull
    private String fechaEnvio;
    @Enumerated(EnumType.STRING)
    private OrdenEstadoEnum estado;

    public static ResponseFechasEnvio crearFechaEnvioResponse(FechaEnvios dataFechaEnvio) {
        return ResponseFechasEnvio
                .builder()
                .fecha(dataFechaEnvio.getFechaEnvio())
                .estado(dataFechaEnvio.getEstado().name())
                .build();
    }
}
