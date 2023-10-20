package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import msUsers.domain.responses.DTOs.DireccionDTO;


@Entity
@Table(name = "Direcciones")
@Data
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

   // @NotNull
    private String codigoPostal;

    public DireccionDTO toDTO(){
        DireccionDTO direccionDTO = new DireccionDTO();
        direccionDTO.setCalle(calle);
        direccionDTO.setPiso(piso);
        direccionDTO.setAltura(altura);
        direccionDTO.setLocalidad(localidad);
        direccionDTO.setCodigoPostal(codigoPostal);
        return direccionDTO;
    }
}
