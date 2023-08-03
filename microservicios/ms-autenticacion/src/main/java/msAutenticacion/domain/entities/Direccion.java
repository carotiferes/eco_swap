package msAutenticacion.domain.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Direcciones")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long idDireccion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Usuario usuario;

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
