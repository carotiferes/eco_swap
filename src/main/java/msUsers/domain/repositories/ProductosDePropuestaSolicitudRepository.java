package msUsers.domain.repositories;

import msUsers.domain.entities.Perfil;
import msUsers.domain.entities.PropuestaProductos;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosDePropuestaSolicitudRepository extends JpaRepositoryImplementation<PropuestaProductos, Long> {


}
