package msTransacciones.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import msTransacciones.domain.entities.enums.EstadoTrueque;
import msTransacciones.domain.responses.DTOs.TruequeDTO;

@Entity
@Data
@Table(name = "Trueques")
public class Trueque {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_trueque")
    private long idTrueque;

    @ManyToOne
    @JoinColumn(name = "publicacion_origen_id")
    private Publicacion publicacionOrigen;

    @ManyToOne
    @JoinColumn(name = "publicacion_propuesta_id")
    private Publicacion publicacionPropuesta;

    @Enumerated(EnumType.STRING)
    private EstadoTrueque estadoTrueque;

    public TruequeDTO toDTO() {
        TruequeDTO truequeDTO = new TruequeDTO();
        truequeDTO.setIdTrueque(idTrueque);
        truequeDTO.setEstadoTrueque(estadoTrueque);
        truequeDTO.setPublicacionDTOpropuesta(publicacionPropuesta.toDTO());
        truequeDTO.setPublicacionDTOorigen(publicacionOrigen.toDTO());
        return truequeDTO;
    }

}
