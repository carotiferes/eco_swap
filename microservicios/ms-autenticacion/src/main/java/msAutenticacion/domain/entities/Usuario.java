package msAutenticacion.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msAutenticacion.domain.responses.DTOs.UsuarioDTO;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Data
@Table(name = "Usuarios")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
    @Size(max = 10)
    private String salt;

    private String jwtPrivateKey;

    @NotNull
    @Size(max = 6)
    private String confirmCodigo;

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

    @Column(columnDefinition = "boolean default 1")
    private boolean bloqueado;

    @Column(name = "validado", columnDefinition = "boolean default 0")
    private boolean validado;

    @OneToMany(mappedBy = "usuarioOpina", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Opinion> opiniones;

    public void aumentarIntentoEn1() {
        this.setIntentos(intentos+1);
    }

    public UsuarioDTO toDTO(){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(idUsuario);
        usuarioDTO.setPuntaje(puntaje);
        usuarioDTO.setEmail(email);
        usuarioDTO.setSwapper(isSwapper());
        usuarioDTO.setTelefono(telefono);
        usuarioDTO.setUsername(username);
        usuarioDTO.setBloqueado(bloqueado);
        usuarioDTO.setValidado(validado);
        usuarioDTO.setIntentos(intentos);
        return usuarioDTO;
    }

}
