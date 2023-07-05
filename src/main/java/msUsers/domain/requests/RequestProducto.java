package msUsers.domain.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;

@Builder
public class RequestProducto {

    @NotNull(message = "El producto debe tener un tipo que lo especifique.")
    private TipoProducto tipoProducto;

    @NotNull(message = "El producto debe poseer una descripción.")
    private String descripcion;

    @NotNull
    @Positive(message = "La cantidad requerida debe ser mayor a cero.")
    private int cantidadRequerida;

    @NotNull(message = "El producto debe tener un estado asignado.")
    private String estado;

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(int cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
