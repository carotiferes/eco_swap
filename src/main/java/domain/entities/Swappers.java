package domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "Swappers")
@PrimaryKeyJoinColumn(name = "idPerfil")
public class Swappers extends Perfil{
    @NotNull
    @Size(max = 40)
    private String nombre;
    @NotNull
    @Size(max = 40)
    private String apellido;
    @NotNull
    @Column(columnDefinition = "DATE")
    private LocalDate fechaNacimiento;

    @OneToOne
    @JoinColumn(name = "id_perfil", referencedColumnName = "idPerfil")
    private Perfil perfil;
}

