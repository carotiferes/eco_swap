package msUsers.domain.repositories;


import msUsers.domain.entities.Fundacion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FundacionesRepository extends JpaRepositoryImplementation<Fundacion, Long> {
}
