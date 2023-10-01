package msTransacciones.domain.repositories;


import msTransacciones.domain.entities.Fundacion;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface FundacionesRepository extends JpaRepositoryImplementation<Fundacion, Long> {
}
