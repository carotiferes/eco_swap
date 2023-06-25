package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Table(name = "Perfiles")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idPerfil")
    private long idPerfil;

    @NotNull
    @Column(unique = true)
    @Size(min = 6, max = 50)
    private String username;

    @NotNull
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL)
    private List<Direccion> direcciones;

    @NotNull
    @Size(min = 8, max = 80)
    private String password;

    @NotNull
    @Size(max = 10)
    private String telefono;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @Range(min = 0, max = 100)
    private String puntaje;
    @NotNull
    private boolean isSwapper; // Un enum?

    @OneToMany(mappedBy = "perfilOpina", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Opinion> opiniones;

    public long getIdPerfil() {return idPerfil;}

    public void setIdPerfil(long idPerfil) {this.idPerfil = idPerfil;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }

    public boolean isSwapper() {return isSwapper;}

    public void setSwapper(boolean swapper) {isSwapper = swapper;}

    public List<Opinion> getOpiniones() {
        return opiniones;
    }

    public void setOpiniones(List<Opinion> opiniones) {
        this.opiniones = opiniones;
    }
}
