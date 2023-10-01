package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.MensajeRespuesta;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRespuestaRepository extends JpaRepositoryImplementation<MensajeRespuesta, Long> {
}
