package msUsers.domain.repositories;

import msUsers.domain.entities.Particular;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticularesRepository extends JpaRepositoryImplementation<Particular, Long> {
}
