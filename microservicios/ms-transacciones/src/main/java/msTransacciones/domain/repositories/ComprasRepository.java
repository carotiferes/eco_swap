package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.Compra;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprasRepository extends JpaRepositoryImplementation<Compra, Long> {
}
