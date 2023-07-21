package msAutenticacion.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import msAutenticacion.domain.entities.enums.TipoDocumento;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Particulares")
@Data
@Builder
public class Particular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idParticular;

    @OneToOne
    @JoinColumn(name = "id_particular")
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

    @Enumerated(value = EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @OneToMany(mappedBy = "particular",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publicacion> publicaciones;
    @OneToMany(mappedBy = "particular",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Donacion> donaciones;
}
