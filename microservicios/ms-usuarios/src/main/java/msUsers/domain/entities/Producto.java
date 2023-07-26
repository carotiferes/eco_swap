package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;

import java.util.List;

@Entity
@Data
@Table(name = "Productos")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idProducto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idProducto;

    @Enumerated(value = EnumType.STRING)
    private TipoProducto tipoProducto;

    @NotNull
    private String descripcion;

    private int cantidadSolicitada;
    private int cantidadRecibida;

    //ToDo: Chequear si esto es un enum. (El mismo de EstadoDonacion?)
    private String estado;

    @OneToMany(mappedBy = "producto",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Donacion> donaciones;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_colecta")
   @JsonBackReference
    private Colecta colecta;

    @ManyToOne(cascade = CascadeType.ALL)
    private Publicacion publicacion;

}
