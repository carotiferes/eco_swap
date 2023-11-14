package msAutenticacion.domain.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msAutenticacion.domain.responses.DTOs.DireccionDTO;

import java.util.Objects;


@Entity
@Table(name = "Direcciones")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idDireccion;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Usuario usuario;

    @NotNull
    private String calle;
    @NotNull
    private String altura;
    @Column(nullable = true)
    private String piso;

    @Column(nullable = true)
    private String dpto;
    @NotNull
    private String localidad;

    @NotNull
    private String codigoPostal;

    private String horarioDisponibilidad;

    public DireccionDTO toDTO(){
        DireccionDTO direccionDTO = new DireccionDTO();
        direccionDTO.setCalle(calle);
        direccionDTO.setPiso(piso);
        direccionDTO.setAltura(altura);
        direccionDTO.setLocalidad(localidad);
        direccionDTO.setCodigoPostal(codigoPostal);
        direccionDTO.setHorarioDisponibilidad(horarioDisponibilidad);
        return direccionDTO;
    }
}
