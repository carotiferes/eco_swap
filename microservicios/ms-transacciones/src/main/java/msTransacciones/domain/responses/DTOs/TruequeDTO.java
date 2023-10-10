package msTransacciones.domain.responses.DTOs;

import lombok.Data;
import msTransacciones.domain.entities.enums.EstadoTrueque;

@Data
public class TruequeDTO {

    private Long idTrueque;
    private EstadoTrueque estadoTrueque;
    private PublicacionDTO publicacionDTOorigen;
    private PublicacionDTO publicacionDTOpropuesta;
}
