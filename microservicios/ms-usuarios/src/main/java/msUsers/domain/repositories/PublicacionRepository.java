package msUsers.domain.repositories;

import msUsers.domain.entities.Publicacion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository extends JpaRepositoryImplementation<Publicacion, Long> {
}
