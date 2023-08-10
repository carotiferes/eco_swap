package msAutenticacion.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Opiniones")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Opinion{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
}
