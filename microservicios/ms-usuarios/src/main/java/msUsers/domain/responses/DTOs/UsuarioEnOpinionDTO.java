package msUsers.domain.responses.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class UsuarioEnOpinionDTO {

    private long idUsuario;
    private String nombre;
    private String apellido;
    private String username;
    private List<DireccionDTO> direcciones;
    private String telefono;
    private String email;
    private float puntaje;
    private boolean isSwapper;
    private Integer intentos;
    private boolean validado;
    private boolean bloqueado;
}
