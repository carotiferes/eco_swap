package msUsers.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import msUsers.domain.entities.enums.EstadoPropuesta;
import msUsers.domain.entities.enums.TipoProducto;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "PropuestasSolicitud")
@AllArgsConstructor
@NoArgsConstructor
public class PropuestaSolicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long idSolicitud;
    private Long idSwapper;

    private TipoProducto tipoProducto;
    private Integer cantidadOfrecida;
    private String mensaje;
    private String caracteristicas;
    private EstadoPropuesta estadoPropuesta;
    private byte[] imagenB64;

    private LocalDateTime fechaYHora;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MensajeRespuesta> respuesta;

    public void addNuevaRespuesta(MensajeRespuesta nuevaRespuesta) {
        this.getRespuesta().add(nuevaRespuesta);
    }
}
