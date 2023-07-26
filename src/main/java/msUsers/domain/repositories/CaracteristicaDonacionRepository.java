package msUsers.domain.repositories;

import msUsers.domain.entities.CaracteristicaDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaracteristicaDonacionRepository extends JpaRepository<CaracteristicaDonacion, Long> {


}
