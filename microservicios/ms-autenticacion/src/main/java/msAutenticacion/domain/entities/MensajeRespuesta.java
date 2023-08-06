package msAutenticacion.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "mensajesRespuesta")
@AllArgsConstructor
@NoArgsConstructor
public class MensajeRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long idEmisor;

    private String mensaje;

    private LocalDateTime fechaYHora;

}
