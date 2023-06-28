package msUsers.domain.entities;

import jakarta.persistence.*;
import msUsers.domain.entities.enums.EstadoPropuesta;

import java.util.List;

@Entity
@Table(name = "Propuestas")
public class Propuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idPropuesta;

    private String descripcion;
    private int cantidadPropuesta;
    @Enumerated(value = EnumType.STRING)
    private EstadoPropuesta estadoPropuesta;

    @ManyToOne(cascade = CascadeType.ALL)
    private Swapper swapper;

    @ManyToOne(cascade = CascadeType.ALL)
    private Producto producto;

    private List<String> imagenes;
}
