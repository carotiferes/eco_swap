package msUsers.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.MensajeChat;
import msUsers.domain.entities.Trueque;
import msUsers.domain.entities.Usuario;
import msUsers.domain.repositories.MensajesChatRepository;
import msUsers.domain.repositories.TruequesRepository;
import msUsers.domain.repositories.UsuariosRepository;
import msUsers.domain.requests.RequestMensajeChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private TruequesRepository truequesRepository;

    @Autowired
    private MensajesChatRepository mensajesChatRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ChatService chatService;

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Sesion {} iniciada, uri {}", session.getId(), session.getUri());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("Sesion {} finalizada con status {}, reason {}", session.getId(), status.getCode(), status.getReason());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Long idUsuarioEmisor = Long.valueOf((String) session.getAttributes().get("usuario"));
        Usuario usuarioEmisor = this.usuariosRepository.findById(idUsuarioEmisor)
                .orElseThrow(() -> new EntityNotFoundException("¡No se encontró el usuario: " + idUsuarioEmisor));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RequestMensajeChat requestMensajeChat = objectMapper.readValue(message.getPayload(), RequestMensajeChat.class);

        log.info("Guardando el mensaje {} del usuario {}", requestMensajeChat.getMensaje(), usuarioEmisor.getUsername());
        Trueque trueque = this.truequesRepository.findById(requestMensajeChat.getIdTrueque())
                .orElseThrow(() -> new EntityNotFoundException("¡No se encontró el trueque: " + requestMensajeChat.getIdTrueque()));
        Usuario usuarioReceptor = this.usuariosRepository.findById(requestMensajeChat.getUsuarioReceptor())
                .orElseThrow(() -> new EntityNotFoundException("¡No se encontró el usuario: " + requestMensajeChat.getUsuarioReceptor()));

        MensajeChat mensaje = new MensajeChat();
        mensaje.setUsuarioEmisor(usuarioEmisor);
        mensaje.setUsuarioReceptor(usuarioReceptor);
        mensaje.setMensaje(requestMensajeChat.getMensaje());
        mensaje.setFechaHoraEnvio(requestMensajeChat.getFechaHoraEnvio());
        mensaje.setTrueque(trueque);

        this.chatService.guardarMensaje(mensaje);
        log.info("Mensaje guardado correctamente.");

        for(WebSocketSession webSocketSession : sessions)
            webSocketSession.sendMessage(message);
    }
}
