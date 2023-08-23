package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msUsers.domain.responses.DTOs.ColectaDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public ColectaDTO toDTO(boolean includeProductos) {
        ColectaDTO colectaDTO = new ColectaDTO();
        colectaDTO.setIdColecta(idColecta);
        colectaDTO.setFundacionDTO(fundacion.toDTO());
        colectaDTO.setTitulo(titulo);
        colectaDTO.setImagen(imagen);
        colectaDTO.setDescripcion(descripcion);
        colectaDTO.setActiva(activa);
        colectaDTO.setFechaInicio(fechaInicio);
        colectaDTO.setFechaFin(fechaFin);

        if (includeProductos && productos != null) {
            colectaDTO.setProductos(productos.stream().map(producto -> producto.toDTO(false)).collect(Collectors.toList())); // Evitar recursión en ProductoDTO
        }

        return colectaDTO;
    }
}
