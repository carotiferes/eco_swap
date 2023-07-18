package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Swappers")
@Data
public class Swapper{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idSwapper;

    @OneToOne
    @JoinColumn(name = "id_perfil")
    private Perfil perfil;

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

    @OneToMany(mappedBy = "swapper",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones;
    @OneToMany(mappedBy = "swapper",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Propuesta> propuestas;
}

