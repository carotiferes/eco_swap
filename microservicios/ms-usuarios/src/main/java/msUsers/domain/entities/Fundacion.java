package msUsers.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import msUsers.domain.responses.DTOs.FundacionDTO;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Fundaciones")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idFundacion")
public class Fundacion implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private List<Colecta> colectas;

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
