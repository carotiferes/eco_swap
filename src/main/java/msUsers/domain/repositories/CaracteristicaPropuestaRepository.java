package msUsers.domain.repositories;

import msUsers.domain.entities.CaracteristicaPropuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaPropuestaRepository extends JpaRepository<CaracteristicaPropuesta, Long> {


}
