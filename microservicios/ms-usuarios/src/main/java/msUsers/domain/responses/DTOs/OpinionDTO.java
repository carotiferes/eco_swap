package msUsers.domain.responses.DTOs;

import lombok.Data;
import msUsers.domain.entities.Usuario;

@Data
public class OpinionDTO {
    private long idOpinion;
    private float valoracion;
    private String descripcion;

    //ToDo: Agregar el control de recursividad entre los DTOs de Opinion y Usuario (usuarioOpina y usuarioOpinado)
}
