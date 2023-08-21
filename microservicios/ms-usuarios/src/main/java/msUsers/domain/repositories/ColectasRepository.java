package msUsers.domain.repositories;

import msUsers.domain.entities.Colecta;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ColectasRepository extends JpaRepositoryImplementation<Colecta, Long> {
}
