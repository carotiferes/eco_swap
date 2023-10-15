package msTransacciones.domain.repositories;

import msTransacciones.domain.entities.OrdenDeEnvio;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenesRepository  extends JpaRepositoryImplementation<OrdenDeEnvio, Long> {

    List<OrdenDeEnvio> findByIdUsuarioOrigen(Long idUsuarioOrigen);
}
