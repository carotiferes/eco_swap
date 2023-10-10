package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.Publicacion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionesRepository extends JpaRepositoryImplementation<Publicacion, Long> {
}
