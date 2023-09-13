package msUsers.controllers;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Producto;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.Trueque;
import msUsers.domain.entities.enums.EstadoTrueque;
import msUsers.domain.repositories.PublicacionesRepository;
import msUsers.domain.repositories.TruequesRepository;
import msUsers.domain.requests.RequestTrueque;
import msUsers.domain.requests.trueques.RequestCambiarEstadoTrueque;
import msUsers.domain.responses.DTOs.PublicacionDTO;
import msUsers.domain.responses.DTOs.TruequeDTO;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.domain.responses.ResponseUpdateEntity;
import msUsers.services.TruequeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class TruequeController {
    @Autowired
    private TruequesRepository truequesRepository;
    @Autowired
    private PublicacionesRepository publicacionesRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TruequeService truequeService;

    private static final String json = "application/json";

    @GetMapping(path = "/trueque/{id_trueque}", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TruequeDTO> getTruequePorId(@PathVariable("id_trueque") Long id) {
        final var trueque = this.truequesRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el trueque: " + id));
        TruequeDTO truequeDTO = trueque.toDTO();
        log.info(">> Se retorna el trueque de ID: {}", truequeDTO.getIdTrueque());
        return ResponseEntity.ok(truequeDTO);
    }

    @GetMapping(path = "/trueque/{id_trueque}/publicacion/{id_publicacion}", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TruequeDTO>> getTruequesXIdPublicacionOrigen(@PathVariable("id_publicacion") Long id) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trueque> query = criteriaBuilder.createQuery(Trueque.class);
        Root<Trueque> from = query.from(Trueque.class);

        Join<Trueque, Publicacion> publicacionOrigenJoin = from.join("publicacionOrigen");
        Predicate predicate = criteriaBuilder.equal(publicacionOrigenJoin.get("idPublicacion"), id);

        query.where(predicate);
        List<Trueque> trueques = entityManager.createQuery(query).getResultList();
        List<TruequeDTO> truequesDTO = trueques.stream().map(Trueque::toDTO).toList();

        log.info(">> Se retornan {} trueques.", truequesDTO.size());
        return ResponseEntity.ok(truequesDTO);
    }

    @GetMapping(path = "/trueque/{id_trueque}/publicaciones", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PublicacionDTO>> listPublicacionesParaTrueque(@PathVariable("id_trueque") Long id) {

        log.info(">> Se va a buscar publicaciones para hacer trueque con la publicacion: {}", id);

        final var publicacion = this.publicacionesRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicacion: " + id));

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Publicacion> query = criteriaBuilder.createQuery(Publicacion.class);
        Root<Publicacion> root = query.from(Publicacion.class);

        Predicate predicate = criteriaBuilder.and(
                criteriaBuilder.lessThanOrEqualTo(root.get("valorTruequeMin"), publicacion.getValorTruequeMax()),
                criteriaBuilder.greaterThanOrEqualTo(root.get("valorTruequeMax"), publicacion.getValorTruequeMin()),
                criteriaBuilder.notEqual(root.get("idPublicacion"),publicacion.getIdPublicacion())
        );

        query.where(predicate);

        List<Publicacion> publicaciones = entityManager.createQuery(query).getResultList();
        List<PublicacionDTO> publicacionesDTO = publicaciones.stream().map(Publicacion::toDTO).toList();

        log.info(">> Encontradas {} publicaciones para efectuar trueque", publicaciones.size());
        return ResponseEntity.ok(publicacionesDTO);
    }

    @PutMapping(path = "/trueque/{id_trueque}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> cambiarEstadoTrueque(@PathVariable("id_trueque") Long idTrueque,
                                                                     @RequestBody @Valid RequestCambiarEstadoTrueque request) {

        log.info(">> Se va cambiar el estado del trueque {} a {}", idTrueque, request.getNuevoEstado());
        final var trueque = this.truequesRepository.findById(idTrueque).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada el trueque: " + idTrueque));

        EstadoTrueque anteriorEstado = trueque.getEstadoTrueque();

        try {
            EstadoTrueque nuevoEstado = EstadoTrueque.valueOf(request.getNuevoEstado());
        } catch (IllegalArgumentException  e) {
            log.error(request.getNuevoEstado() + " no es un estado valido");
            ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
            responseUpdateEntity.setStatus(HttpStatus.BAD_REQUEST.name());
            responseUpdateEntity.setDescripcion(request.getNuevoEstado() + " no es un estado valido");
            return ResponseEntity.badRequest().body(responseUpdateEntity);
        }

        if(EstadoTrueque.valueOf(request.getNuevoEstado()).equals(EstadoTrueque.RECHAZADO)) {
            EstadoTrueque nuevoEstado = EstadoTrueque.valueOf(request.getNuevoEstado());
            trueque.setEstadoTrueque(nuevoEstado);
        } else {
            if (EstadoTrueque.valueOf(request.getNuevoEstado()).equals(EstadoTrueque.APROBADO)) {
                trueque.setEstadoTrueque(EstadoTrueque.APROBADO);

                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<Trueque> query = criteriaBuilder.createQuery(Trueque.class);
                Root<Trueque> from = query.from(Trueque.class);

                Predicate predicate = criteriaBuilder.or(

                        criteriaBuilder.and(
                                criteriaBuilder.equal(from.get("publicacionOrigen"), trueque.getPublicacionOrigen()),
                                criteriaBuilder.notEqual(from.get("idTrueque"), idTrueque)
                        ),

                        criteriaBuilder.or(
                                criteriaBuilder.equal(from.get("publicacionOrigen"), trueque.getPublicacionPropuesta()),
                                criteriaBuilder.equal(from.get("publicacionPropuesta"), trueque.getPublicacionOrigen())
                        )
                );

                query.where(predicate);

                List<Trueque> truequesRelacionados = entityManager.createQuery(query).getResultList();
                truequesRelacionados.forEach(t -> t.setEstadoTrueque(EstadoTrueque.ANULADO));
                truequesRelacionados.forEach(entityManager::merge);
            }
            else {
                trueque.setEstadoTrueque(EstadoTrueque.valueOf(request.getNuevoEstado()));
            }
        }

        this.truequesRepository.save(trueque);

        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        responseUpdateEntity.setStatus(HttpStatus.OK.name());
        responseUpdateEntity.setDescripcion("Se cambio el estado del trueque de " + anteriorEstado + " a " + request.getNuevoEstado());
        return ResponseEntity.ok(responseUpdateEntity);
    }

    @PostMapping(path = "/trueque", produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> crearTrueque(@RequestBody @Valid RequestTrueque request) {

        log.info(">> Trueque con la publicacion origen {} y la publicacion propuesta {}", request.getIdPublicacionOrigen(), request.getIdPublicacionPropuesta());
        final var publicacionOrigen = this.publicacionesRepository.findById(request.getIdPublicacionOrigen()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicacion origen: " + request.getIdPublicacionOrigen()));
        final var publicacionPropuesta = this.publicacionesRepository.findById(request.getIdPublicacionPropuesta()).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicacion propuesta: " + request.getIdPublicacionPropuesta()));

        if(truequeService.existeTruequeConPublicaciones(publicacionOrigen.getIdPublicacion(), publicacionPropuesta.getIdPublicacion()))
            throw new EntityExistsException("Ya existe un trueque entre ambas publicaciones.");

        Trueque trueque = new Trueque();
        trueque.setEstadoTrueque(EstadoTrueque.PENDIENTE);
        trueque.setPublicacionOrigen(publicacionOrigen);
        trueque.setPublicacionPropuesta(publicacionPropuesta);

        var entity = this.truequesRepository.save(trueque);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setId(entity.getIdTrueque());
        responsePostEntityCreation.setDescripcion("Trueque creado.");
        responsePostEntityCreation.setStatus(HttpStatus.OK.name());
        log.info("<< Trueque creado con ID: {}", entity.getIdTrueque());

        return ResponseEntity.created(location).body(responsePostEntityCreation);
    }

}
