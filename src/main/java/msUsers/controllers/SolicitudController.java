package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.criteria.*;
import msUsers.domain.entities.*;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.repositories.PerfilRepository;
import msUsers.domain.repositories.PropuestasRepository;
import msUsers.domain.repositories.SolicitudRepository;
import msUsers.domain.requests.RequestFilterSolicitudes;
import msUsers.domain.requests.propuestas.RequestComunicarPropuestaSolicitudModel;
import msUsers.domain.requests.propuestas.RequestMensajeRespuesta;
import msUsers.domain.requests.RequestSolicitud;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.services.ImageService;
import msUsers.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import msUsers.domain.responses.ResponseSolicitudesList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class SolicitudController {
    @Autowired SolicitudRepository solicitudRepository;
    @Autowired FundacionesRepository fundacionesRepository;
    @Autowired PropuestasRepository propuestasRepository;
    @Autowired EntityManager entityManager;
    @Autowired
    PerfilRepository perfilRepository;
    @Autowired
    SolicitudService solicitudService;
    @Autowired
    ImageService imageService;

    //private UserContextService userContextService;

    private static final String json = "application/json";


    @PostMapping(path = "/solicitud", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createSolicitud(@Valid @RequestBody RequestSolicitud requestSolicitud) {

        //final var perfil = this.userContextService.getPerfil();
        log.info(">> Request de creación de solicitud: {}", requestSolicitud.getTitulo());

        var fundacionOptional = this.fundacionesRepository.findById(requestSolicitud.getIdFundacion());
        Fundacion fundacion = fundacionOptional.orElseThrow(() -> new EntityNotFoundException("¡La fundacion no existe!"));

        Solicitud solicitud = new Solicitud();
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setActiva(true);
        solicitud.setDescripcion(requestSolicitud.getDescripcion());
        solicitud.setFundacion(fundacion);
        solicitud.setTitulo(requestSolicitud.getTitulo());

        String img = requestSolicitud.getImagen();
        solicitud.setImagen(imageService.saveImage(img));

        List<Producto> productos = requestSolicitud.getProductos().stream()
                .map(reqProducto -> {
                    Producto p = new Producto();
                    p.setSolicitud(solicitud);
                    p.setDescripcion(reqProducto.getDescripcion());
                    p.setCantidadSolicitada(reqProducto.getCantidadRequerida());
                    p.setTipoProducto(reqProducto.getTipoProducto());
                    return p;
                }).collect(Collectors.toList());

        solicitud.setProductos(productos);

        // Guardo la solicitud y me quedo con el id generada en la base de datos
        var entity = this.solicitudRepository.save(solicitud);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setId(entity.getIdSolicitud());
        responsePostEntityCreation.setDescripcion("Solicitud creada.");
        responsePostEntityCreation.setStatus(HttpStatus.OK.name());
        log.info("<< Solicitud creada con ID: {}", entity.getIdSolicitud());

        return ResponseEntity.created(location).body(responsePostEntityCreation);
    }

    /*
    COMUNICACION DE PROPUESTAS
     */

    @PostMapping(path = "/solicitud/{idSolicitud}/propuestas", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> crearComunicacionDePropuesta(
            @PathVariable(required = true) Long idSolicitud,
            @Valid @RequestBody RequestComunicarPropuestaSolicitudModel request) throws Exception {
        log.info(">> Request para solicitud ID {} comunicar propuesta: {}", idSolicitud, request.toString());
        solicitudService.crearPropuestaComunicacion(request, idSolicitud);
        ResponsePostEntityCreation response = new ResponsePostEntityCreation();
        response.setId(idSolicitud);
        response.setDescripcion("Comunicación creada.");
        response.setStatus(HttpStatus.OK.name());
        log.info("<< Comunicacion creada en la solicitud: {}", idSolicitud);
        return ResponseEntity.ok(response);
    }

    /* POSTERGAMOS ESTE DESARROLLO
    @PutMapping(path = "/solicitud/{idSolicitud}/propuestas/{idPropuestaComunicacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Object> agregarMensajeComunicacionDePropuesta(
            @PathVariable(required = true) Long idSolicitud,
            @PathVariable(required = true) Long idPropuestaComunicacion,
            @Valid @RequestBody RequestMensajeRespuesta request) {
        log.info(">> Request para idPropuestaComunicacion {} con mensaje para comunicar propuesta: {}",
                idPropuestaComunicacion, request.toString());
        solicitudService.agregarMensajeParaPropuestaComunicacion(request, idSolicitud, idPropuestaComunicacion);
        log.info("<< Mensaje añadido para idPropuestaComunicacion {}", idPropuestaComunicacion);
        return ResponseEntity.ok().build();
    }
     */

    @GetMapping(path = "/solicitud/{idSolicitud}/propuestas", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public List<Propuesta> obtenerTodasLasComunicacionesDeSolicitud(@PathVariable(required = true) Long idSolicitud)  {
        log.info(">> Request obtener todas las comunicanes de propuesta: {}", idSolicitud);
        List<Propuesta> propuestaSolicitudsList = solicitudService.obtenerTodasLasPropuestasComunicacion(idSolicitud);
        log.info("<< Cantidad de propuestas obtenidas: {} para idSolicitud: {}",
                propuestaSolicitudsList.size(),
                idSolicitud);
        return propuestaSolicitudsList;
    }

    @GetMapping(path = "/solicitud/{idSolicitud}/propuestas/{idPropuesta}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public Propuesta obtenerComunicacionesDeSolicitudXIdComunicacion(@PathVariable(required = true) Long idPropuesta) {
        log.info(">> Request obtener comunicacion de propuesta x idPropuesta: {}", idPropuesta);
        Propuesta propuestaSolicitud = solicitudService.obtenerPropuestasComunicacionXId(idPropuesta);
        log.info("<< Propuesta obtenido: {}", propuestaSolicitud);
        return propuestaSolicitud;
    }

    @GetMapping(path = "/solicitudes", produces = json)
    public ResponseEntity<List<ResponseSolicitudesList>> listSolicitudes(@ModelAttribute RequestFilterSolicitudes request) {
        log.info(">> Se realiza listado de solicitudes con los parametros: {}", request);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Solicitud> query = cb.createQuery(Solicitud.class);
        Root<Solicitud> from = query.from(Solicitud.class);
        Predicate predicate = cb.conjunction();

        if (request.getIdFundacion() != null) {
            Join<Solicitud, Fundacion> join = from.join("fundacion");
            predicate = cb.and(predicate, cb.equal(join.get("idFundacion"), request.getIdFundacion()));
        }
        if (request.getCodigoPostal() != null) {
            Join<Solicitud, Fundacion> fundacionJoin = from.join("fundacion");
            Join<Fundacion, Perfil> perfilJoin = fundacionJoin.join("perfil");
            Join<Perfil, Direccion> direccionJoin = perfilJoin.join("direcciones");
            predicate = cb.and(predicate, cb.equal(direccionJoin.get("codigoPostal"), request.getCodigoPostal()));
        }

        if (request.getTipoProducto() != null) {
            Join<Solicitud, Producto> join = from.join("productos");
            predicate = cb.and(predicate, cb.equal(join.get("tipoProducto"), request.getTipoProducto()));
        }

        query.where(predicate);

        List<Solicitud> solicitudes = entityManager.createQuery(query).getResultList();

        List<ResponseSolicitudesList> solicitudesDTO = solicitudes.stream().map(solicitud -> {
            ResponseSolicitudesList responseSolicitudesList = new ResponseSolicitudesList();
            responseSolicitudesList.setTituloSolicitud(solicitud.getTitulo());
            responseSolicitudesList.setFundacion(solicitud.getFundacion().getNombre());
            responseSolicitudesList.setProductos(solicitud.getProductos());
            responseSolicitudesList.setIdSolicitud(solicitud.getIdSolicitud());
            responseSolicitudesList.setImagen(solicitud.getImagen());
            responseSolicitudesList.setIdFundacion(solicitud.getFundacion().getIdFundacion());
            return responseSolicitudesList;
        }).collect(Collectors.toList());

        log.info("<< {} solicitudes encontradas.", solicitudesDTO.size());
        return ResponseEntity.ok(solicitudesDTO);
    }

    @GetMapping(path = "/solicitud/{id_solicitud}", produces = json)
    public ResponseEntity<Solicitud> getSolicitud(@PathVariable("id_solicitud") Long id) {
        final var solicitud = this.solicitudRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la solicitud: " + id));
        return ResponseEntity.ok(solicitud);
    }

}

