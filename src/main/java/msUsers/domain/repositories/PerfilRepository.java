package msUsers.domain.repositories;

import msUsers.domain.entities.Perfil;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PerfilRepository extends JpaRepositoryImplementation<Perfil, Long> {

}
