package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idSolicitud;

    @Size(max = 50)
    @NotNull
    private String titulo;
    private String descripcion;

    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate fechaSolicitud;

    // ToDo: Chequear que en el DER figura en vez de boolean, como código. ¿Por qué?
    private boolean activa;

    @NotNull
    private String imagen;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Fundacion fundacion;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private List<Producto> productos;
}
