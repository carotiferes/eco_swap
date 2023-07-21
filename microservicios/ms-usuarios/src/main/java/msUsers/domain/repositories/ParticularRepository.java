package msUsers.domain.repositories;

import msUsers.domain.entities.Particular;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticularRepository extends JpaRepositoryImplementation<Particular, Long> {
}
