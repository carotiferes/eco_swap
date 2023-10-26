package msUsers.domain.repositories;

import msUsers.domain.entities.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionesRepository extends JpaRepository<Notificacion, Long> {


}
