package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.criteria.*;
import msUsers.domain.entities.*;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.ColectasRepository;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.requests.RequestFilterColectas;
import msUsers.domain.requests.RequestColecta;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.services.CriteriaBuilderQueries;
import msUsers.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import msUsers.domain.responses.DTOs.ColectaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class ColectaController {
    @Autowired
    ColectasRepository colectasRepository;
    @Autowired FundacionesRepository fundacionesRepository;
    @Autowired EntityManager entityManager;
    @Autowired
    ImageService imageService;

    @Autowired
    CriteriaBuilderQueries criteriaBuilderQueries;

    private static final String json = "application/json";

    @PostMapping(path = "/colecta", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createColecta(@Valid @RequestBody RequestColecta requestColecta) {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Fundacion> optionalFundacion = criteriaBuilderQueries.getFundacionPorUsuario(user.getIdUsuario());
        Fundacion fundacion = optionalFundacion.orElseThrow(() -> new EntityNotFoundException("¡La fundacion no existe!"));

        //final var usuario = this.jwtService.getParticularPorJwt(requestColecta.getJwt());
        log.info(">> Request de creación de colecta: {}", requestColecta.getTitulo());

        Colecta colecta = new Colecta();
        colecta.setFechaInicio(requestColecta.getFechaInicio());
        colecta.setFechaFin(requestColecta.getFechaFin());
        colecta.setActiva(true);
        colecta.setDescripcion(requestColecta.getDescripcion());
        colecta.setFundacion(fundacion);
        colecta.setTitulo(requestColecta.getTitulo());

        List<Producto> productos = requestColecta.getProductos().stream()
                .map(reqProducto -> {
                    Producto p = new Producto();
                    p.setColecta(colecta);
                    p.setDescripcion(reqProducto.getDescripcion());

                    Optional<Integer> cantidadRequerida = Optional.ofNullable(reqProducto.getCantidadRequerida());
                    cantidadRequerida.ifPresent(p::setCantidadSolicitada);

                    p.setTipoProducto(reqProducto.getTipoProducto());
                    return p;
                }).collect(Collectors.toList());

        colecta.setProductos(productos);

        // Guardo la colecta y me quedo con el id generada en la base de datos
        var entity = this.colectasRepository.save(colecta);

        String img = requestColecta.getImagen();
        colecta.setImagen(imageService.saveImage(img));
        entity = this.colectasRepository.save(colecta); // Actualizo la colecta con la imagen

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setId(entity.getIdColecta());
        responsePostEntityCreation.setDescripcion("Colecta creada.");
        responsePostEntityCreation.setStatus(HttpStatus.OK.name());
        log.info("<< Colecta creada con ID: {}", entity.getIdColecta());

        return ResponseEntity.created(location).body(responsePostEntityCreation);
    }

    @GetMapping(path = "/colectas", produces = json)
    public ResponseEntity<List<ColectaDTO>> listColectas(@ModelAttribute RequestFilterColectas request) {
        log.info(">> Se realiza listado de donaciones con los parametros: {}", request);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Colecta> query = cb.createQuery(Colecta.class);
        Root<Colecta> from = query.from(Colecta.class);
        Predicate predicate = cb.conjunction();

        if (request.getIdFundacion() != null) {
            Join<Colecta, Fundacion> join = from.join("fundacion");
            predicate = cb.and(predicate, cb.equal(join.get("idFundacion"), request.getIdFundacion()));
        }
        if (request.getCodigoPostal() != null) {
            Join<Colecta, Fundacion> fundacionJoin = from.join("fundacion");
            Join<Fundacion, Usuario> usuarioJoin = fundacionJoin.join("usuario");
            Join<Usuario, Direccion> direccionJoin = usuarioJoin.join("direcciones");
            predicate = cb.and(predicate, cb.equal(direccionJoin.get("codigoPostal"), request.getCodigoPostal()));
        }

        if (request.getTipoProducto() != null) {
            Join<Colecta, Producto> join = from.join("productos");
            predicate = cb.and(predicate, cb.equal(join.get("tipoProducto"), request.getTipoProducto()));
        }

        query.where(predicate);

        List<Colecta> colectas = entityManager.createQuery(query).getResultList();

        List<ColectaDTO> colectasDTO = colectas.stream().map(Colecta::toDTO).collect(Collectors.toList());

        log.info("<< {} colectas encontradas.", colectasDTO.size());
        return ResponseEntity.ok(colectasDTO);
    }

    @GetMapping(path = "/colecta/{id_colecta}", produces = json)
    public ResponseEntity<ColectaDTO> getColecta(@PathVariable("id_colecta") Long id) {
        final var colecta = this.colectasRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la colecta: " + id));
        ColectaDTO colectaDTO = colecta.toDTO();
        return ResponseEntity.ok(colectaDTO);
    }

    @GetMapping(path = "/colecta", produces = json)
    public ResponseEntity<List<ColectaDTO>> getColectaPorIdFundacion() {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Fundacion> optionalFundacion = criteriaBuilderQueries.getFundacionPorUsuario(user.getIdUsuario());
        Fundacion fundacion = optionalFundacion.orElseThrow(() -> new EntityNotFoundException("¡La fundacion no existe!"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Colecta> query = cb.createQuery(Colecta.class);
        Root<Colecta> from = query.from(Colecta.class);
        Predicate predicate = cb.conjunction();

        Join<Colecta, Fundacion> join = from.join("fundacion");
        predicate = cb.and(predicate, cb.equal(join.get("idFundacion"), fundacion.getIdFundacion()));

        query.where(predicate);

        List<Colecta> colectas = entityManager.createQuery(query).getResultList();
        List<ColectaDTO> colectasDTO = colectas.stream().map(Colecta::toDTO).toList();

        return ResponseEntity.ok(colectasDTO);
    }
    @GetMapping(path = "/misColectas", produces = json)
    public ResponseEntity<List<ColectaDTO>> getMisColectas() {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Fundacion> optionalFundacion = criteriaBuilderQueries.getFundacionPorUsuario(user.getIdUsuario());
        Fundacion fundacion = optionalFundacion.orElseThrow(() -> new EntityNotFoundException("¡La fundacion no existe!"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Colecta> query = cb.createQuery(Colecta.class);
        Root<Colecta> from = query.from(Colecta.class);
        Predicate predicate = cb.conjunction();

        Join<Colecta, Fundacion> join = from.join("fundacion");
        predicate = cb.and(predicate, cb.equal(join.get("idFundacion"), fundacion.getIdFundacion()));

        query.where(predicate);

        List<Colecta> colectas = entityManager.createQuery(query).getResultList();
        List<ColectaDTO> colectasDTO = colectas.stream().map(Colecta::toDTO).toList();

        return ResponseEntity.ok(colectasDTO);
    }


}

