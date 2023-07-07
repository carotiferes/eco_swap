package msUsers.domain.repositories;

import msUsers.domain.entities.Perfil;
import msUsers.domain.entities.PropuestaSolicitud;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropuestaSolicitudRepository extends JpaRepositoryImplementation<PropuestaSolicitud, Long> {

    List<PropuestaSolicitud> findByIdSolicitud(Long idSolicitud);

}
