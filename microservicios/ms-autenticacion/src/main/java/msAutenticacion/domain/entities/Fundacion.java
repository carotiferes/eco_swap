package msAutenticacion.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msAutenticacion.domain.responses.DTOs.FundacionDTO;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Fundaciones")
@Data
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idFundacion")
@AllArgsConstructor
@NoArgsConstructor
public class Fundacion implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long idFundacion;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @NotNull
    @Size(max = 60)
    private String nombre;

    @NotNull
    @Size(max = 13)
    @Column(unique = true)
    private String cuil;

    private boolean baja;

    @OneToMany(mappedBy = "fundacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Colecta> solicitudes;

    public FundacionDTO toDTO() {
        FundacionDTO fundacionDTO = new FundacionDTO();
        fundacionDTO.setIdFundacion(idFundacion);
        fundacionDTO.setNombre(nombre);
        fundacionDTO.setCuil(cuil);
        fundacionDTO.setPuntaje(usuario.getPuntaje());
        fundacionDTO.setDirecciones(usuario.getDirecciones().stream().map(Direccion::toDTO).toList());
        return fundacionDTO;
    }

}
