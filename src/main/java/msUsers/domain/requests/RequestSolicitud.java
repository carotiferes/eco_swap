package msUsers.domain.requests;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.entities.Producto;

import java.time.LocalDate;
import java.util.List;

public class RequestSolicitud {

    @NotNull
    @Size(max = 50)
    private String titulo;
    @NotNull
    private String descripcion;
    @NotNull
    private long idFundacion;
    @NotNull
    private List<RequestProducto> productos;

    // ToDo: Definir contra el front
    private List<String> imagenes;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getIdFundacion() {
        return idFundacion;
    }

    public void setIdFundacion(long idFundacion) {
        this.idFundacion = idFundacion;
    }

    public List<RequestProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<RequestProducto> productos) {
        this.productos = productos;
    }
}
