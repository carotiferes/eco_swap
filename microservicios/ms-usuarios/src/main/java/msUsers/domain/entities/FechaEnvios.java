package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.logistica.enums.EstadoEnvio;
import msUsers.domain.responses.DTOs.FechaEnviosDTO;
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
    private EstadoEnvio estado;

    public static ResponseFechasEnvio crearFechaEnvioResponse(FechaEnvios dataFechaEnvio) {
        return ResponseFechasEnvio
                .builder()
                .fecha(dataFechaEnvio.getFechaEnvio())
                .estado(dataFechaEnvio.getEstado().name())
                .build();
    }

    public FechaEnviosDTO toDTO() {
        FechaEnviosDTO fechaEnviosDTO = new FechaEnviosDTO();
        fechaEnviosDTO.setIdOrden(idOrden);
        fechaEnviosDTO.setFechaEnvio(fechaEnvio);
        fechaEnviosDTO.setEstado(estado);
        return fechaEnviosDTO;
    }
}
