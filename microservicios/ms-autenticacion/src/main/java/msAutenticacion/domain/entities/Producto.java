package msAutenticacion.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import msAutenticacion.domain.entities.enums.TipoProducto;

import java.util.List;

@Entity
@Data
@Table(name = "Productos")
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    private List<Donacion> donaciones;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_colecta")
    @JsonBackReference
    private Colecta colecta;

    @ManyToOne(cascade = CascadeType.ALL)
    private Publicacion publicacion;

}
