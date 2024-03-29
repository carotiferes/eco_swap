package msUsers.domain.repositories;

import msUsers.domain.entities.OrdenDeEnvio;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenesRepository extends JpaRepositoryImplementation<OrdenDeEnvio, Long> {

    List<OrdenDeEnvio> findByIdUsuarioOrigen(Long idUsuarioOrigen);
    List<OrdenDeEnvio> findByIdUsuarioDestino(Long idUsuarioDestino);
}
