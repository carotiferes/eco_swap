package msAutenticacion.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "Colectas")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idColecta")
@JsonIgnoreProperties("productos")
public class Colecta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idColecta;

    @Size(max = 50)
    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @Column(columnDefinition = "DATE")
    private LocalDate fechaInicio;
    @Column(columnDefinition = "DATE")
    private LocalDate fechaFin;

    private boolean activa;

    @Lob
    @Column(length = 100000)
    private String imagen;

    @ManyToOne(cascade = CascadeType.ALL)
    private Fundacion fundacion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "colecta")
    private List<Producto> productos;

}
