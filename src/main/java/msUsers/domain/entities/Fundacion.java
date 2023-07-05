package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "Fundaciones")
public class Fundacion{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFundacion;

    @OneToOne
    @JoinColumn(name = "id_perfil")
    private Perfil perfil;

    @NotNull
    @Size(max = 60)
    private String nombre;

    @NotNull
    @Size(max = 11)
    @Column(unique = true)
    private String cuil;

    private boolean baja;

    @OneToMany(mappedBy = "fundacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Solicitud> solicitudes;


    public long getIdFundacion() {
        return idFundacion;
    }

    public void setIdFundacion(long idFundacion) {
        this.idFundacion = idFundacion;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public List<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }
}
