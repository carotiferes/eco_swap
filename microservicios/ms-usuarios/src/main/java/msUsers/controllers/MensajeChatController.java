package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Compra;
import msUsers.domain.entities.MensajeChat;
import msUsers.domain.entities.Usuario;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.MensajesChatRepository;
import msUsers.domain.repositories.TruequesRepository;
import msUsers.domain.repositories.UsuariosRepository;
import msUsers.domain.requests.RequestMensajeChat;
import msUsers.domain.responses.DTOs.MensajeChatDTO;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.domain.responses.ResponseUpdateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class MensajeChatController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private TruequesRepository truequesRepository;
    @Autowired
    private MensajesChatRepository mensajesChatRepository;

    private static final String json = "application/json";

    @GetMapping(path = "/chat/{id_trueque}", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<MensajeChatDTO>> getConversacionXTrueque(@PathVariable("id_trueque") Long idTrueque) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MensajeChat> query = cb.createQuery(MensajeChat.class);
        Root<MensajeChat> from = query.from(MensajeChat.class);
        Predicate predicate = cb.equal(from.get("trueque").get("idTrueque"), idTrueque);

        query.where(predicate);

        List<MensajeChat> conversacion = entityManager.createQuery(query).getResultList();
        List<MensajeChatDTO> conversacionDTO = conversacion.stream().map(MensajeChat::toDTO).sorted(Comparator.comparing(MensajeChatDTO::getFechaHoraEnvio)).collect(Collectors.toList());

        log.info("<< Se retorna la conversación del trueque {}. Total de mensajes: {}", idTrueque, conversacion.size());
        return ResponseEntity.ok(conversacionDTO);
    }

    @PostMapping(path = "/chat", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> enviarMensajeConversacion(@RequestBody @Valid RequestMensajeChat requestMensajeChat) {

        Usuario usuarioEmisor = UsuarioContext.getUsuario();
        final var usuarioReceptor = this.usuariosRepository.findById(requestMensajeChat.getUsuarioReceptor()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + requestMensajeChat.getUsuarioReceptor()));
        final var trueque = this.truequesRepository.findById(requestMensajeChat.getIdTrueque()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el trueque: " + requestMensajeChat.getIdTrueque()));


        MensajeChat mensaje = new MensajeChat();
        mensaje.setMensaje(requestMensajeChat.getMensaje());
        mensaje.setTrueque(trueque);
        mensaje.setFechaHoraEnvio(LocalDateTime.now());
        mensaje.setUsuarioEmisor(usuarioEmisor);
        mensaje.setUsuarioReceptor(usuarioReceptor);
        this.mensajesChatRepository.save(mensaje);

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setStatus(HttpStatus.OK.name());
        responsePostEntityCreation.setDescripcion("Mensaje enviado correctamente.");

        return ResponseEntity.ok(responsePostEntityCreation);
    }

}
