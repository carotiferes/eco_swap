package msTransacciones.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import msTransacciones.domain.entities.enums.TipoProducto;
import msTransacciones.domain.responses.DTOs.ProductoDTO;

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
    private Colecta colecta;

    @ManyToOne(cascade = CascadeType.ALL)
    private Publicacion publicacion;

    public ProductoDTO toDTO(boolean includeColecta) {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setIdProducto(idProducto);
        productoDTO.setTipoProducto(tipoProducto);
        productoDTO.setDescripcion(descripcion);
        productoDTO.setTipoProducto(tipoProducto);
        productoDTO.setCantidadSolicitada(cantidadSolicitada);
        productoDTO.setCantidadRecibida(cantidadRecibida);

        if (includeColecta && colecta != null) {
            productoDTO.setColectaDTO(colecta.toDTO(false)); // Evitar recursión en ColectaDTO
        }

        return productoDTO;
    }

}
