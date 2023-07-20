package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Data
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idUsuario")
    private long idUsuario;

    @NotNull
    @Column(unique = true)
    @Size(min = 6, max = 50)
    private String username;

    //  @NotNull
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
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
    private boolean isSwapper;

    @OneToMany(mappedBy = "usuarioOpina", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Opinion> opiniones;

}
