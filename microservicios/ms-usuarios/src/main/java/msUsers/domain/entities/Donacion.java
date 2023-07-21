package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.entities.enums.EstadoDonacion;

import java.util.List;

@Entity
@Table(name = "Donaciones")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idDonacion")
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idDonacion;

    private String descripcion;
    private int cantidadDonacion;

    @Enumerated(value = EnumType.STRING)
    private EstadoDonacion estadoDonacion;

    @ManyToOne(cascade = CascadeType.ALL)
    private Particular particular;

    @ManyToOne(cascade = CascadeType.ALL)
    private Producto producto;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CaracteristicaPropuesta> caracteristicaPropuesta;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Colecta colecta;

    private String imagenes;
}
