package msUsers.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idDireccion;

    @ManyToOne(cascade = CascadeType.ALL)
    private Perfil perfil;

    @NotNull
    private String direccion;
    @NotNull
    private String altura;
    @Column(nullable = true)
    private String piso;

    @Column(nullable = true)
    private String dpto;

    @NotNull
    private String codigoPostal;
}
