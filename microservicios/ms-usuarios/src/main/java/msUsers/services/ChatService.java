package msUsers.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import msUsers.domain.entities.MensajeChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    private EntityManager entityManager; // Inyecta el EntityManager

    @Transactional
    public void guardarMensaje(MensajeChat mensaje) {
        this.entityManager.merge(mensaje);
    }
}
