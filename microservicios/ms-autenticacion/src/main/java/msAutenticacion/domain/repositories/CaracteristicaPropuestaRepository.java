package msAutenticacion.domain.repositories;

import msAutenticacion.domain.entities.CaracteristicaPropuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaPropuestaRepository extends JpaRepository<CaracteristicaPropuesta, Long> {


}
