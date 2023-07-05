package msUsers.domain.responses;

import msUsers.domain.entities.Producto;

import java.util.List;

public class ResponseSolicitudesList {

    private long idSolicitud;
    private String fundacion;
    private String tituloSolicitud;
    private List<Producto> productos;
    private int cantidadDisponible;
    private String imagen;

    public String getFundacion() {
        return fundacion;
    }

    public void setFundacion(String fundacion) {
        this.fundacion = fundacion;
    }

    public long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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
