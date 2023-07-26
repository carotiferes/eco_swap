package msAutenticacion.domain.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Fundaciones")
@Data
@Builder
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
    @Size(max = 11)
    @Column(unique = true)
    private String cuil;

    private boolean baja;

    @OneToMany(mappedBy = "fundacion", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Colecta> solicitudes;

}
