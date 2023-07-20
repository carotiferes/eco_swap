package msUsers.domain.repositories;

import msUsers.domain.entities.Usuario;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepositoryImplementation<Usuario, Long> {

}
