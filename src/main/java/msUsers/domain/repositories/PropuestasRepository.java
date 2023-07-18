package msUsers.domain.repositories;

import msUsers.domain.entities.Propuesta;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropuestasRepository extends JpaRepositoryImplementation<Propuesta, Long> {

}
