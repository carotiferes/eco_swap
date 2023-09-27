package msAutenticacion.components;

import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.exceptions.events.UsuarioCreadoEvent;
import msAutenticacion.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EmailConfirmationListener {

    @Autowired
    private EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUsuarioCreadoEvent(UsuarioCreadoEvent event) {
        Usuario usuarioCreado = event.getUsuarioCreado();
        emailService.sendConfirmEmail(usuarioCreado.getEmail(), "SUBJECT 2", usuarioCreado, usuarioCreado.getConfirmCodigo());
    }
}


