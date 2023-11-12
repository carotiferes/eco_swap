package msUsers.domain.responses.DTOs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msUsers.domain.entities.FechaEnvios;
import msUsers.domain.entities.ProductosADonarDeOrden;

import java.util.List;

@Data
public class OrdenDeEnvioDTO {

    private long idOrden;
    private float precioEnvio;
    private String titulo;
    private String descripcion;
    private Long idUsuarioOrigen;
    private Long idUsuarioDestino;
    private String nombreCalle;
    private String altura;
    private String piso;
    private String dpto;
    private String barrio;
    private String ciudad;
    private String codigoPostal;
    private String nombreUserDestino;
    private String nombreUserOrigen;
    private String telefono;
    private List<ProductosADonarDeOrdenDTO> productosADonarDeOrdenList;
    private Long publicacionId;
    private Long colectaId;
    private Boolean esPublicacion;
    private List<FechaEnviosDTO> listaFechaEnvios;
    private String fechaADespachar;
}
