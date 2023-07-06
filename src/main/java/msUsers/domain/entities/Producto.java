package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;

import java.util.List;

@Entity
@Data
@Table(name = "Productos")
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

    //ToDo: Chequear si esto es un enum. (El mismo de EstadoPropuesta?)
    private String estado;

    @OneToMany(mappedBy = "producto",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Propuesta> propuestas;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Solicitud solicitud;

    @ManyToOne(cascade = CascadeType.ALL)
    private Publicacion publicacion;

}
