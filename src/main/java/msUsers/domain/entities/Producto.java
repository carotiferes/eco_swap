package msUsers.domain.entities;

import jakarta.persistence.*;
import msUsers.domain.entities.enums.TipoProductos;

import java.util.List;

@Entity
@Table(name = "Productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idProducto;

    @Enumerated(value = EnumType.STRING)
    private TipoProductos tipoProductos;

    private String descripcion;

    private int cantidadSolicitada;
    private int cantidadRecibida;

    //ToDo: Chequear si esto es un enum. (El mismo de EstadoPropuesta?)
    private String estado;

    @OneToMany(mappedBy = "producto",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Propuesta> propuestas;

    @ManyToOne(cascade = CascadeType.ALL)
    private Solicitud solicitud;

    @ManyToOne(cascade = CascadeType.ALL)
    private Publicacion publicacion;


}
