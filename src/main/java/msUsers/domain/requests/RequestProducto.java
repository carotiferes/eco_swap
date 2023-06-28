package msUsers.domain.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import msUsers.domain.entities.enums.TipoProducto;

public class RequestProducto {

    @NotNull
    private TipoProducto tipoProducto;

    @NotNull
    private String descripcion;

    @NotNull
    @Positive
    private int cantidadRequerida;

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
}
