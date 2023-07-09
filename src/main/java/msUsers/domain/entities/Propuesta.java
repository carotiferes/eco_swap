package msUsers.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.entities.enums.EstadoPropuesta;

import java.util.List;

@Entity
@Table(name = "Propuestas")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
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

    @OneToMany(cascade = CascadeType.ALL)
    private List<CaracteristicaPropuesta> caracteristicaPropuesta;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Solicitud solicitud;

    private List<String> imagenes;
}
