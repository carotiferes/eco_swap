package msUsers.domain.responses.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class FundacionDTO {
    private String nombre;
    private Long idFundacion;
    private String cuil;
    private Integer puntaje;
    private List<DireccionDTO> direcciones;
    private UsuarioDTO usuarioDTO;
}
