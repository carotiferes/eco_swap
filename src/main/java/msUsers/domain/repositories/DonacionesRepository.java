package msUsers.domain.repositories;

import msUsers.domain.entities.Donacion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface DonacionesRepository extends JpaRepositoryImplementation<Donacion, Long> {

}
