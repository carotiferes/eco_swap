package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "Ordenes")
@Builder
@Data
public class OrdenDeEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idOrden;
    @NotNull
    private float precioEnvio;

    @Size(max = 100)
    private String titulo;
    @Size(max = 300)
    private String descripcion;

    @NotNull
    private Long idUsuarioOrigen;

    @NotNull
    private Long idUsuarioDestino;

    @NotNull
    private String nombreCalle;
    @NotNull
    private String altura;
    @NotNull
    private String piso;
    @NotNull
    private String dpto;
    @NotNull
    private String barrio;
    @NotNull
    private String ciudad;
    @NotNull
    private String codigoPostal;
    @NotNull
    private String nombreUserDestino;
    @NotNull
    private String nombreUserOrigen;
    @NotNull
    private Integer cantidad;
    @NotNull
    private String telefono;
    @NotNull
    private Long productoId;
    @NotNull
    private Long publicacionColectaId;
    @NotNull
    private Boolean esPublicacion;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FechaEnvios> listaFechaEnvios;

}
