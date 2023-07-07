package msUsers.domain.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.enums.TipoProducto;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "PropuestasSolicitud")
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
    private byte[] imagenB64;

    private LocalDateTime fechaYHora;

    @OneToMany(mappedBy = "propuestaSolicitud",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MensajeRespuesta> respuesta;
}
