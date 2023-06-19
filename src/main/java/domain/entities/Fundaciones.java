package domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Fundaciones")
@PrimaryKeyJoinColumn(name = "idPerfil")
public class Fundaciones extends Perfil{
    @NotNull
    @Size(max = 60)
    private String razonSocial;  // Razón social?

    @OneToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "idPerfil")
    private Perfil perfil;
}
