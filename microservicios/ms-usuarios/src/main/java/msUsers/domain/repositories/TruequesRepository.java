package msUsers.domain.repositories;

import msUsers.domain.entities.Trueque;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface TruequesRepository extends JpaRepositoryImplementation<Trueque, Long> {
}
