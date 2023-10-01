package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.Usuario;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepositoryImplementation<Usuario, Long> {

}
