package msAutenticacion.domain.repositories;

import msAutenticacion.domain.entities.Colecta;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ColectaRepository extends JpaRepositoryImplementation<Colecta, Long> {
}
