package msUsers.domain.repositories;

import msUsers.domain.entities.Opinion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionesRepository extends JpaRepositoryImplementation<Opinion, Long> {
}