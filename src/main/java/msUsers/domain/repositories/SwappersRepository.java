package msUsers.domain.repositories;

import msUsers.domain.entities.Swapper;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SwappersRepository extends JpaRepositoryImplementation<Swapper, Long> {
}
