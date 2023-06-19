package domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Perfiles")
@Inheritance(strategy = InheritanceType.JOINED)
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idPerfil")
    private UUID idPerfil;

    @NotNull
    @Column(unique = true)
    @Size(min = 6, max = 50)
    private String username;

    @NotNull
    @Size(min = 8, max = 80)
    private String password;

    @NotNull
    @Size(max = 10)
    private int telefono;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @NotNull
    private String direccion;
    private float puntaje;

    @NotNull
    private boolean swapper;

    @OneToMany(mappedBy = "perfil", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Opinion> opiniones;

    public UUID getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(UUID idPerfil) {
        this.idPerfil = idPerfil;
    }

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

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(float puntaje) {
        this.puntaje = puntaje;
    }

    public boolean isSwapper() {
        return swapper;
    }

    public void setSwapper(boolean swapper) {
        this.swapper = swapper;
    }

    public List<Opinion> getOpiniones() {
        return opiniones;
    }

    public void setOpiniones(List<Opinion> opiniones) {
        this.opiniones = opiniones;
    }
}
