package msUsers.domain.repositories;

import msUsers.domain.entities.Compra;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprasRepository extends JpaRepositoryImplementation<Compra, Long> {
}
