package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Particulares")
@Data
public class Particular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idParticular;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @NotNull
    @Size(max = 40)
    private String nombre;
    @NotNull
    @Size(max = 40)
    private String apellido;

    @NotNull
    @Size(max = 8)
    private String dni;

    @NotNull
    @Size(max = 11)
    private String cuil;

    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate fechaNacimiento;

    @OneToOne(cascade = CascadeType.ALL)
    private TipoDocumento tipoDocumento;

    @OneToMany(mappedBy = "particular",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones;
    @OneToMany(mappedBy = "particular",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Donacion> donaciones;
}

