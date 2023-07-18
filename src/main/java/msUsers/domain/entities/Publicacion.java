package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import msUsers.domain.entities.enums.EstadoPublicacion;

import java.util.List;

@Entity
@Table(name = "Publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idPublicacion;

    @Size(max = 50)
    private String titulo;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoPublicacion;

    @ManyToOne(cascade = CascadeType.ALL)
    private Swapper swapper;

    @OneToMany(mappedBy = "publicacion", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Producto> productos;

}
