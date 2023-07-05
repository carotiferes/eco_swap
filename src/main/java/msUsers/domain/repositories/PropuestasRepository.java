package msUsers.domain.repositories;

import msUsers.domain.entities.Propuesta;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface PropuestasRepository extends JpaRepositoryImplementation<Propuesta, Long> {
}
