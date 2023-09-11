package msUsers.domain.responses.DTOs;

import lombok.Data;

@Data
public class TruequeDTO {

    private Long idTrueque;
    private PublicacionDTO publicacionDTOorigen;
    private PublicacionDTO publicacionDTOpropuesta;
}
