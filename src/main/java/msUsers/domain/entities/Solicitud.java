package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idSolicitud;

    @Size(max = 50)
    @NotNull
    private String titulo;

    @NotNull
    private String descripcion;

    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate fechaSolicitud;

    // ToDo: Chequear que en el DER figura en vez de boolean, como código. ¿Por qué?
    private boolean activa;

    @NotNull
    private String imagen;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Fundacion fundacion;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_solicitud")
    @JsonManagedReference
    private List<Producto> productos;

    public long getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(long idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

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

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Fundacion getFundacion() {
        return fundacion;
    }

    public void setFundacion(Fundacion fundacion) {
        this.fundacion = fundacion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
