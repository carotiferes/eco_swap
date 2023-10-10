package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.Colecta;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ColectasRepository extends JpaRepositoryImplementation<Colecta, Long> {
}
