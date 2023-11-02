package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Entity
@Table(name = "Ordenes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
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

    private String piso;

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
    private String telefono;

    @OneToMany(mappedBy = "ordenDeEnvio",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JsonManagedReference
    private List<ProductosADonarDeOrden> productosADonarDeOrdenList;

    private Long publicacionId;

    private Long colectaId;
    @NotNull
    private Boolean esPublicacion;

    @OneToMany(cascade = CascadeType.ALL)
    private List<FechaEnvios> listaFechaEnvios;

    private String fechaADespachar;

}
