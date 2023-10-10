package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.Donacion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface DonacionesRepository extends JpaRepositoryImplementation<Donacion, Long> {

}
