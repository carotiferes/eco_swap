package msUsers.domain.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public class RequestSolicitud {

    @NotNull(message = "La solicitud debe contener un título.")
    @Size(max = 50)
    private String titulo;

    @NotNull(message = "La solicitud debe contener una descripción.")
    private String descripcion;

    private long idFundacion;

    @NotNull(message = "La solicitud debe tener una lista de productos.")
    private List<RequestProducto> productos;

    private String imagen;

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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
