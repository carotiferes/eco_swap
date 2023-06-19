package domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "Opiniones")
public class Opinion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idOpinion;


    // private UUID idOpinado; Posible futura clave compuesta?

    @NotNull
    private float valoracion;

    @Size(max = 250)
    private String descripcion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;
}
