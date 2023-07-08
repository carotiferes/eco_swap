package msUsers.domain.repositories;

import msUsers.domain.entities.MensajeRespuesta;
import msUsers.domain.entities.Perfil;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRespuestaRepository extends JpaRepositoryImplementation<MensajeRespuesta, Long> {
}
