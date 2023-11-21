package msUsers.domain.responses.DTOs;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class OpinionDTO {
    private long idOpinion;
    private float valoracion;
    private String descripcion;
    private UsuarioEnOpinionDTO usuarioOpina;
    private UsuarioEnOpinionDTO usuarioOpinado;
    private ZonedDateTime fechaHoraOpinion;
}
