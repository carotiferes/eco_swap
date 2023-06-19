package domain.repositories;

import domain.entities.Fundaciones;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FundacionesRepository extends JpaRepositoryImplementation<Fundaciones, UUID> {
}
