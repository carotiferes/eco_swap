package msUsers.domain.repositories;


import msUsers.domain.entities.Direccion;
import msUsers.domain.entities.Fundacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundacionesRepository extends JpaRepositoryImplementation<Fundacion, Long> {
}
