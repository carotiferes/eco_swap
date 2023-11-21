package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msUsers.converters.ZonedDateTimeConverter;
import msUsers.domain.responses.DTOs.OpinionDTO;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "Opiniones")
public class Opinion{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idOpinion;
    @NotNull
    private float valoracion;

    @Size(max = 250)
    private String descripcion;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario_opina", nullable = false)
    private Usuario usuarioOpina;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario_opinado", nullable = false)
    private Usuario usuarioOpinado;

    @Column(columnDefinition = "TIMESTAMP")
    @Convert(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime fechaHoraOpinion;

    public OpinionDTO toDTO(){
        OpinionDTO opinionDTO = new OpinionDTO();
        opinionDTO.setIdOpinion(idOpinion);
        opinionDTO.setDescripcion(descripcion);
        opinionDTO.setValoracion(valoracion);
        opinionDTO.setUsuarioOpina(usuarioOpina.toUsuarioEnOpinionDTO());
        opinionDTO.setUsuarioOpinado(usuarioOpinado.toUsuarioEnOpinionDTO());
        opinionDTO.setFechaHoraOpinion(fechaHoraOpinion);
        return opinionDTO;
    }
}
