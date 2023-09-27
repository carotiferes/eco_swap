package msUsers.domain.repositories;

import msUsers.domain.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosRepository extends JpaRepository<Producto, Long> {

}
