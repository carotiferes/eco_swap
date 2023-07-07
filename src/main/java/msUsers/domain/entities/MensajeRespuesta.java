package msUsers.domain.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "mensajesRespuesta")
public class MensajeRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String mensaje;

    private LocalDateTime fechaYHora;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PropuestaSolicitud propuestaSolicitud;
}
