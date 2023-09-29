package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msUsers.domain.responses.DTOs.UsuarioDTO;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Data
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
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
    @Size(max = 12)
    private String telefono;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @Range(min = 0, max = 5)
    private Integer puntaje;

    @NotNull
    private boolean isSwapper;

    @Column(columnDefinition = "int default 0")
    private Integer intentos;

    @Column(name = "validado", columnDefinition = "boolean default 0")
    private boolean validado;

    @Column(columnDefinition = "boolean default 1")
    private boolean bloqueado;

    @OneToMany(mappedBy = "usuarioOpina", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Opinion> opiniones;

    public UsuarioDTO toDTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(idUsuario);
        usuarioDTO.setUsername(username);
        usuarioDTO.setDirecciones(direcciones.stream().map(Direccion::toDTO).toList());
        usuarioDTO.setTelefono(telefono);
        usuarioDTO.setEmail(email);
        usuarioDTO.setPuntaje(puntaje);
        usuarioDTO.setSwapper(isSwapper);
        usuarioDTO.setIntentos(intentos);
        usuarioDTO.setValidado(validado);
        usuarioDTO.setBloqueado(bloqueado);
        usuarioDTO.setOpiniones(opiniones.stream().map(Opinion::toDTO).toList());
        return usuarioDTO;
    }

}
