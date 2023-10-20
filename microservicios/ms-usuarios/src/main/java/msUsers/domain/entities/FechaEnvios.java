package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.responses.logisticaResponse.EnumEstadoOrden;

@Entity
@Table(name = "FechaEnvios")
@Builder
@Data
public class FechaEnvios {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idOrden;
    @NotNull
    private String fechaEnvio;
    @Enumerated(EnumType.STRING)
    private EnumEstadoOrden estado;
}
