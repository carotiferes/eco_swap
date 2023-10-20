package msAutenticacion.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Opiniones")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private LocalDateTime fechaHoraOpinion;

}
