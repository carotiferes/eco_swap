package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "Direcciones")
@Data
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idDireccion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
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
