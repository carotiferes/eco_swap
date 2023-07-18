package msUsers.domain.repositories;

import msUsers.domain.entities.Solicitud;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepositoryImplementation<Solicitud, Long> {
}
