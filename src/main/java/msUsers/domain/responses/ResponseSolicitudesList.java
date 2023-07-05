package msUsers.domain.responses;

import msUsers.domain.entities.Producto;

import java.util.List;

public class ResponseSolicitudesList {
    private String fundacion;
    private String tituloSolicitud;
    private List<Producto> productos;
    private int cantidadDisponible;

    public String getFundacion() {
        return fundacion;
    }

    public void setFundacion(String fundacion) {
        this.fundacion = fundacion;
    }

    public String getTituloSolicitud() {
        return tituloSolicitud;
    }

    public void setTituloSolicitud(String tituloSolicitud) {
        this.tituloSolicitud = tituloSolicitud;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
}
