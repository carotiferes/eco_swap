package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.EstadoPublicacion;
import msUsers.domain.entities.enums.TipoPublicacion;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "Publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idPublicacion;

    @Size(max = 50)
    private String titulo;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoPublicacion;

    private TipoPublicacion tipoPublicacion;

    private LocalDate fechaPublicacion;

    private Double precioVenta;
    private Double valorTruequeMax;
    private Double valorTruequeMin;

    @ManyToOne(cascade = CascadeType.ALL)
    private Particular particular;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CaracteristicaProducto> caracteristicaProducto;

    @OneToMany(mappedBy = "publicacion", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Producto> productos;

    private String imagenes;

}
