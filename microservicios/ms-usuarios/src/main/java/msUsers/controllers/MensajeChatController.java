package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.MensajeChat;
import msUsers.domain.repositories.MensajesChatRepository;
import msUsers.domain.repositories.TruequesRepository;
import msUsers.domain.repositories.UsuariosRepository;
import msUsers.domain.responses.DTOs.MensajeChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Controller
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

        log.info("<< Se retorna la conversacion del trueque {}. Total de mensajes: {}", idTrueque, conversacion.size());
        return ResponseEntity.ok(conversacionDTO);
    }

}
