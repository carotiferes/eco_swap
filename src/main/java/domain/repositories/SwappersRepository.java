package domain.repositories;

import domain.entities.Swappers;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SwappersRepository extends JpaRepositoryImplementation<Swappers, UUID> {
}
