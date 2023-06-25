package msUsers.domain.entities;

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
    private List<Solicitud> solicitudes;

}
