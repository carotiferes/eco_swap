package msAutenticacion.domain.repositories;

import msAutenticacion.domain.entities.Usuario;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepositoryImplementation<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
}
