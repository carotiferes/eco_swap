package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoPublicacion;
import msUsers.domain.entities.enums.EstadoTrueque;
import msUsers.domain.entities.enums.TipoPublicacion;
import msUsers.domain.logistica.enums.EstadoEnvio;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.ParticularesRepository;
import msUsers.domain.repositories.PublicacionesRepository;
import msUsers.domain.requests.RequestFilterPublicaciones;
import msUsers.domain.requests.RequestPublicacion;
import msUsers.domain.responses.DTOs.PublicacionDTO;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.domain.responses.ResponseUpdateEntity;
import msUsers.services.CriteriaBuilderQueries;
import msUsers.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class PublicacionController {

    @Autowired
    PublicacionesRepository publicacionesRepository;
    @Autowired
    ParticularesRepository particularesRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    ImageService imageService;

    @Autowired
    CriteriaBuilderQueries criteriaBuilderQueries;
    private static final String json = "application/json";

    @PostMapping(path = "/publicacion", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createPublicacion(@Valid @RequestBody RequestPublicacion requestPublicacion) {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("¡El particular no existe!"));
        
        log.info(">> Request de creación de la publicación: {}", requestPublicacion.getTitulo());

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(requestPublicacion.getTitulo());
        publicacion.setEstadoPublicacion(EstadoPublicacion.ABIERTA);
        publicacion.setParticular(particular);
        publicacion.setValorTruequeMax(requestPublicacion.getValorTruequeMax());
        publicacion.setValorTruequeMin(requestPublicacion.getValorTruequeMin());
        publicacion.setDescripcion(requestPublicacion.getDescripcion());
        publicacion.setPeso(requestPublicacion.getPeso());
        publicacion.setTipoPublicacion(requestPublicacion.getTipoPublicacion());
        publicacion.setTipoProducto(requestPublicacion.getTipoProducto());

        List<CaracteristicaProducto> listaCaracteristicas = requestPublicacion.getCaracteristicasProducto()
                .stream()
                .map(s -> CaracteristicaProducto.armarCarateristica(s, particular.getIdParticular()))
                .toList();

        publicacion.setCaracteristicaProducto(listaCaracteristicas);
        publicacion.setFechaPublicacion(LocalDate.now());

        if(requestPublicacion.getEsVenta()) {
            publicacion.setTipoPublicacion(TipoPublicacion.VENTA_Y_TRUEQUE);
            publicacion.setPrecioVenta(requestPublicacion.getPrecioVenta());
        }
        else
            publicacion.setTipoPublicacion(TipoPublicacion.TRUEQUE);

        // Guardo la publicacion y me quedo con el id generada en la base de datos
        //var entity = this.publicacionRepository.save(publicacion);

        // ToDo: Cambiar logica para que soporte una falla en la creacion de la publicacion
        List<String> nombreImagenes = new ArrayList<>();
        for (int i = 0; i < requestPublicacion.getImagen().size(); i++) {
            nombreImagenes.add(imageService.saveImage(requestPublicacion.getImagen().get(i)));
        }

        publicacion.setImagenes(String.join("|", nombreImagenes));
        var entity = this.publicacionesRepository.save(publicacion); // Actualizo la publicacion con la imagen

        log.info(">> Publicación creada con ID: {}", entity.getIdPublicacion());

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setId(entity.getIdPublicacion());
        responsePostEntityCreation.setDescripcion("Publicacion creada.");
        responsePostEntityCreation.setStatus(HttpStatus.OK.name());
        log.info("<< Publicacion creada con ID: {}", entity.getIdPublicacion());

        return ResponseEntity.created(location).body(responsePostEntityCreation);

    }

    @GetMapping(path = "/publicaciones", produces = json)
    public ResponseEntity<List<PublicacionDTO>> listPublicaciones(@ModelAttribute RequestFilterPublicaciones request) {
        log.info(">> Se realiza listado de publicaciones con los parametros: {}", request);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Publicacion> query = cb.createQuery(Publicacion.class);
        Root<Publicacion> from = query.from(Publicacion.class);
        Predicate predicate = cb.conjunction();

        if (request.getLocalidades() != null) {
            Join<Publicacion, Particular> particularJoin = from.join("particular");
            Join<Particular, Usuario> usuarioJoin = particularJoin.join("usuario");
            Join<Usuario, Direccion> direccionJoin = usuarioJoin.join("direcciones");

            Expression<String> localidadExpression = direccionJoin.get("localidad");

            predicate = cb.and(predicate, localidadExpression.in(request.getLocalidades()));
        }

        if (request.getTipoProducto() != null) {
            predicate = cb.and(predicate, cb.equal(from.get("tipoProducto"), request.getTipoProducto()));
        }

        predicate = cb.and(predicate, cb.notEqual(from.get("estadoPublicacion"),EstadoPublicacion.CERRADA));

        query.where(predicate);

        List<Publicacion> publicaciones = entityManager.createQuery(query).getResultList();
        List<PublicacionDTO> publicacionesDTOS = publicaciones.stream().map(Publicacion::toDTO).toList();

        log.info("<< {} publicaciones encontradas.", publicacionesDTOS.size());
        return ResponseEntity.ok(publicacionesDTOS);
    }

    @GetMapping(path = "/misPublicaciones", produces = json)
    public ResponseEntity<List<PublicacionDTO>> getMisPublicaciones() {

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("¡El particular no existe!"));

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Publicacion> query = cb.createQuery(Publicacion.class);
        Root<Publicacion> from = query.from(Publicacion.class);
        Predicate predicate = cb.conjunction();

        Join<Publicacion, Particular> join = from.join("particular");
        predicate = cb.and(predicate, cb.equal(join.get("idParticular"), particular.getIdParticular()));

        query.where(predicate);

        List<Publicacion> publicaciones = entityManager.createQuery(query).getResultList();
        List<PublicacionDTO> publicacionesDTO = publicaciones.stream().map(Publicacion::toDTO).toList();

        return ResponseEntity.ok(publicacionesDTO);
    }

    @GetMapping(path = "/publicacion/{id_publicacion}", produces = json)
    public ResponseEntity<PublicacionDTO> getPublicacionXId(@PathVariable("id_publicacion") Long idPublicacion) {
        final var publicacion = this.publicacionesRepository.findById(idPublicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicacion: " + idPublicacion));

        return ResponseEntity.ok(publicacion.toDTO());
    }

    @PatchMapping(path = "/publicacion/{id_publicacion}", produces = json)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> cerrarPublicacion(@PathVariable("id_publicacion") Long idPublicacion) {
        log.info(">> Se va a cerrar la publicacion: {}", idPublicacion);

        final var publicacion = this.publicacionesRepository.findById(idPublicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicacion: " + idPublicacion));

        // Anulo todas sus propuestas de trueques
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Trueque> query = criteriaBuilder.createQuery(Trueque.class);
        Root<Trueque> from = query.from(Trueque.class);

        Predicate predicate = criteriaBuilder.or(
                        criteriaBuilder.equal(from.get("publicacionOrigen"), publicacion),
                        criteriaBuilder.equal(from.get("publicacionPropuesta"), publicacion)
        );

        query.where(predicate);
        List<Trueque> truequesRelacionados = entityManager.createQuery(query).getResultList();
        truequesRelacionados.forEach(t -> t.setEstadoTrueque(EstadoTrueque.ANULADO));
        truequesRelacionados.forEach(entityManager::merge);

        // Cierro publicación
        publicacion.setEstadoPublicacion(EstadoPublicacion.CERRADA);
        entityManager.merge(publicacion);

        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        responseUpdateEntity.setDescripcion("Publicacion y trueques relacionados cerrados exitósamente");
        responseUpdateEntity.setStatus(HttpStatus.OK.name());
        log.info("<< Publicacion cerrada");

        return ResponseEntity.ok(responseUpdateEntity);
    }

    @PatchMapping(path = "/publicacion/{id_publicacion}/recibido", produces = json)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> marcarRecibidoPublicacion(@PathVariable("id_publicacion") Long idPublicacion) {
        log.info(">> Se va a marcar como recibida la publicacion: {}", idPublicacion);

        final var publicacion = this.publicacionesRepository.findById(idPublicacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la publicacion: " + idPublicacion));

        publicacion.setEstadoEnvio(EstadoEnvio.RECIBIDO);
        entityManager.merge(publicacion);

        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        responseUpdateEntity.setDescripcion("Publicación marcada como recibida exitosamente.");
        responseUpdateEntity.setStatus(HttpStatus.OK.name());
        log.info("<< Publicacion marcada como recibida.");

        return ResponseEntity.ok(responseUpdateEntity);
    }
}
