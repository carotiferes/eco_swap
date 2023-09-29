package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import msUsers.domain.responses.DTOs.OpinionDTO;

@Entity
@Table(name = "Opiniones")
public class Opinion{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idOpinion;
    @NotNull
    private float valoracion;

    @Size(max = 250)
    private String descripcion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Usuario usuarioOpina;

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuarioOpinado;

    // ToDo: ¿Las opiniones tendrán fecha?

    public OpinionDTO toDTO(){
        OpinionDTO opinionDTO = new OpinionDTO();
        opinionDTO.setIdOpinion(idOpinion);
        opinionDTO.setDescripcion(descripcion);
        opinionDTO.setValoracion(valoracion);
        return opinionDTO;
    }
}
