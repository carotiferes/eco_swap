package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.OpinionesRepository;
import msUsers.domain.repositories.UsuariosRepository;
import msUsers.domain.requests.RequestNuevaOpinion;
import msUsers.domain.responses.DTOs.OpinionDTO;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.exceptions.OpinionCreationException;
import msUsers.services.CriteriaBuilderQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class OpinionController {

    @Autowired
    private OpinionesRepository opinionesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private CriteriaBuilderQueries criteriaBuilderQueries;

    @Autowired
    private EntityManager entityManager;


    private static final String json = "application/json";
    @GetMapping(path = "/opinion/{id_opinion}", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OpinionDTO> getOpinionXID(@PathVariable("id_opinion") Long id) {
        final var opinion = this.opinionesRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la opinión: " + id));
        OpinionDTO opinionDTO = opinion.toDTO();
        return ResponseEntity.ok(opinionDTO);
    }

    @GetMapping(path = "/misOpiniones", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<OpinionDTO>> getMisOpiniones() {

        final Usuario user = UsuarioContext.getUsuario();


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Opinion> query = cb.createQuery(Opinion.class);
        Root<Opinion> from = query.from(Opinion.class);
        Predicate predicate = cb.conjunction();

        Join<Opinion, Usuario> join = from.join("usuarioOpinado");
        predicate = cb.and(predicate, cb.equal(join.get("idUsuario"), user.getIdUsuario()));

        query.where(predicate);

        List<Opinion> opiniones = entityManager.createQuery(query).getResultList();
        List<OpinionDTO> opinionesDTOS = opiniones.stream().map(Opinion::toDTO).toList();;

        return ResponseEntity.ok(opinionesDTOS);
    }

    @GetMapping(path = "/opiniones/usuario/{id_usuario}", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<OpinionDTO>> getOpinionesDeXUsuario(@PathVariable(name = "id_usuario") Long idUsuarioOpinado) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Opinion> query = cb.createQuery(Opinion.class);
        Root<Opinion> from = query.from(Opinion.class);
        Predicate predicate = cb.conjunction();

        Join<Opinion, Usuario> join = from.join("usuarioOpinado");
        predicate = cb.and(predicate, cb.equal(join.get("idUsuario"), idUsuarioOpinado));

        query.where(predicate);

        List<Opinion> opiniones = entityManager.createQuery(query).getResultList();
        List<OpinionDTO> opinionesDTOS = opiniones.stream().map(Opinion::toDTO).toList();;

        return ResponseEntity.ok(opinionesDTOS);
    }

    @PostMapping(path = "/opiniones/crearOpinion", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createOpinion(@Valid @RequestBody RequestNuevaOpinion requestNuevaOpinion) {

        final Usuario userOpinador = UsuarioContext.getUsuario();

        var usuarioOpinado = this.usuariosRepository.findById(requestNuevaOpinion.getIdUsuarioOpinado()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + requestNuevaOpinion.getIdUsuarioOpinado()));

        if(userOpinador.getIdUsuario() == usuarioOpinado.getIdUsuario())
            throw new OpinionCreationException("No se podés crearte una opinión de ti mismo.");

        Opinion opinion = new Opinion();
        opinion.setDescripcion(requestNuevaOpinion.getDescripcion());
        opinion.setValoracion(requestNuevaOpinion.getValoracion());
        opinion.setFechaHoraOpinion(LocalDateTime.now());
        opinion.setUsuarioOpina(userOpinador);
        opinion.setUsuarioOpinado(usuarioOpinado);
        var entity = this.opinionesRepository.save(opinion);

        // Promediar el puntaje del usuarioOpinado
        float nuevoPuntaje;
        if(usuarioOpinado.getPuntaje() == 0)
            nuevoPuntaje = requestNuevaOpinion.getValoracion();
        else
            nuevoPuntaje = (usuarioOpinado.getPuntaje() + requestNuevaOpinion.getValoracion()) / 2;

        usuarioOpinado.setPuntaje(Math.min(nuevoPuntaje, 5));
        this.usuariosRepository.save(usuarioOpinado);

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setDescripcion("Opinión creada exitosamente.");
        responsePostEntityCreation.setId(entity.getIdOpinion());
        responsePostEntityCreation.setStatus(HttpStatus.CREATED.name());

        return ResponseEntity.ok(responsePostEntityCreation);
    }
}
