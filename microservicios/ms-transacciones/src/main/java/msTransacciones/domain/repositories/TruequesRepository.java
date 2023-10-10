package msTransacciones.domain.repositories;


import msTransacciones.domain.entities.Trueque;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface TruequesRepository extends JpaRepositoryImplementation<Trueque, Long> {
}
