package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.entities.Usuario;

import java.time.LocalDateTime;

@Data
public class OpinionDTO {
    private long idOpinion;
    private float valoracion;
    private String descripcion;
    private UsuarioEnOpinionDTO usuarioOpina;
    private UsuarioEnOpinionDTO usuarioOpinado;
    private LocalDateTime fechaHoraOpinion;
}
