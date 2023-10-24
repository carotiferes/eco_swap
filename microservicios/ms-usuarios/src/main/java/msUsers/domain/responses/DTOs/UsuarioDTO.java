package msUsers.domain.responses.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioDTO {

    private long idUsuario;
    private String username;
    private List<DireccionDTO> direcciones;
    private String telefono;
    private String email;
    private float puntaje;
    private boolean isSwapper;
    private Integer intentos;
    private boolean validado;
    private boolean bloqueado;
    private List<OpinionDTO> opiniones;
    private String avatar;
}
