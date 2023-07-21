package msAutenticacion.domain.repositories;

import msAutenticacion.domain.entities.Particular;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticularRepository extends JpaRepositoryImplementation<Particular, Long> {
}
