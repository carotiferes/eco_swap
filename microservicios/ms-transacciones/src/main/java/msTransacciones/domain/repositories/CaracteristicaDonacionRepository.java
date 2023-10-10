package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.CaracteristicaDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaDonacionRepository extends JpaRepository<CaracteristicaDonacion, Long> {


}
