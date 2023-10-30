package msUsers.domain.repositories;

import msUsers.domain.entities.MensajeChat;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface MensajesChatRepository extends JpaRepositoryImplementation<MensajeChat, Long> {
}
