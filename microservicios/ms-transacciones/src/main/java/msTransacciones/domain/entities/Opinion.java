package msTransacciones.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
}
