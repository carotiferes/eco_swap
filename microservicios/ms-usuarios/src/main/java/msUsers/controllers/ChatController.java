package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.MensajeChat;
import msUsers.domain.entities.Usuario;
import msUsers.domain.entities.enums.EstadoTrueque;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.MensajesChatRepository;
import msUsers.domain.repositories.TruequesRepository;
import msUsers.domain.repositories.UsuariosRepository;
import msUsers.domain.requests.RequestMensajeChat;
import msUsers.exceptions.ChatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private TruequesRepository truequesRepository;
    @Autowired
    private MensajesChatRepository mensajesChatRepository;
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public MensajeChat enviarMensajeConversacion(@Valid RequestMensajeChat requestMensajeChat) {

        Usuario usuarioEmisor = UsuarioContext.getUsuario();
        final var usuarioReceptor = this.usuariosRepository.findById(requestMensajeChat.getUsuarioReceptor()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + requestMensajeChat.getUsuarioReceptor()));
        final var trueque = this.truequesRepository.findById(requestMensajeChat.getIdTrueque()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el trueque: " + requestMensajeChat.getIdTrueque()));

        if(!(trueque.getEstadoTrueque() == EstadoTrueque.APROBADO))
            throw new ChatException("El trueque no está aprobado. No se puede iniciar una conversación hasta que no cambie de estado.");

        MensajeChat mensaje = new MensajeChat();
        mensaje.setMensaje(requestMensajeChat.getMensaje());
        mensaje.setTrueque(trueque);
        mensaje.setFechaHoraEnvio(ZonedDateTime.now());
        mensaje.setUsuarioEmisor(usuarioEmisor);
        mensaje.setUsuarioReceptor(usuarioReceptor);
        //var entitiy = this.mensajesChatRepository.save(mensaje);

        return mensaje;
    }

    @MessageMapping("/chat/addUser")
    @SendTo("/topic/chat")
    public MensajeChat addUser(@Valid RequestMensajeChat requestMensajeChat, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {

        Usuario usuarioEmisor = UsuarioContext.getUsuario();
        final var usuarioReceptor = this.usuariosRepository.findById(requestMensajeChat.getUsuarioReceptor()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + requestMensajeChat.getUsuarioReceptor()));
        final var trueque = this.truequesRepository.findById(requestMensajeChat.getIdTrueque()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el trueque: " + requestMensajeChat.getIdTrueque()));

        if(!(trueque.getEstadoTrueque() == EstadoTrueque.APROBADO))
            throw new ChatException("El trueque no está aprobado. No se puede iniciar una conversación hasta que no cambie de estado.");

        simpMessageHeaderAccessor.getSessionAttributes().put("username", usuarioEmisor.getUsername());

        MensajeChat mensaje = new MensajeChat();
        mensaje.setMensaje(requestMensajeChat.getMensaje());
        mensaje.setTrueque(trueque);
        mensaje.setFechaHoraEnvio(ZonedDateTime.now());
        mensaje.setUsuarioEmisor(usuarioEmisor);
        mensaje.setUsuarioReceptor(usuarioReceptor);
        //var entitiy = this.mensajesChatRepository.save(mensaje);

        return mensaje;
    }
}
