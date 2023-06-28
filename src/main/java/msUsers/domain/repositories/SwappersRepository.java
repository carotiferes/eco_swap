package msUsers.domain.repositories;

import msUsers.domain.entities.Swapper;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface SwappersRepository extends JpaRepositoryImplementation<Swapper, Long> {
}
