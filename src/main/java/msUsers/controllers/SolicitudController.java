package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.repositories.PerfilRepository;
import msUsers.domain.repositories.PropuestasRepository;
import msUsers.domain.repositories.SolicitudRepository;
import msUsers.domain.requests.propuestas.RequestComunicarPropuestaSolicitudModel;
import msUsers.domain.requests.propuestas.RequestMensajeRespuesta;
import msUsers.domain.requests.RequestSolicitud;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
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

    //private UserContextService userContextService;

    private static final String json = "application/json";

    @PostMapping(path = "/solicitud", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createSolicitud(@Valid @RequestBody RequestSolicitud requestSolicitud){

        //final var perfil = this.userContextService.getPerfil();

        var fundacionOptional = this.fundacionesRepository.findById(requestSolicitud.getIdFundacion());
        Fundacion fundacion = fundacionOptional.orElseThrow(() -> new EntityNotFoundException("¡La fundacion no existe!"));

        Solicitud solicitud = new Solicitud();
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setActiva(true);
        solicitud.setDescripcion(requestSolicitud.getDescripcion());
        solicitud.setFundacion(fundacion);
        solicitud.setTitulo(requestSolicitud.getTitulo());
        solicitud.setImagen("imagen.jpg");

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

        return ResponseEntity.created(location).body(responsePostEntityCreation);
    }

    /*
    COMUNICACION DE PROPUESTAS
     */

    @PostMapping(path = "/solicitud/{idSolicitud}/comunicarPropuesta", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void crearComunicacionDePropuesta(
            @PathVariable(required = true) Long idSolicitud,
            @Valid @RequestBody RequestComunicarPropuestaSolicitudModel request) {
        log.info(">> Request para solicitud ID {} comunicacr propuesta: {}", idSolicitud, request.toString());
        solicitudService.crearPropuestaComunicacion(request, idSolicitud);
        log.info("<< Comunicacr creado");
    }

    @PutMapping(path = "/solicitud/{idSolicitud}/comunicarPropuesta/{idPropuestaComunicacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void agregarMensajeComunicacionDePropuesta(
            @PathVariable(required = true) Long idSolicitud,
            @PathVariable(required = true) Long idPropuestaComunicacion,
            @Valid @RequestBody RequestMensajeRespuesta request) {
        log.info(">> Request para idPropuestaComunicacion {} con mensaje para comunicar propuesta: {}",
                idPropuestaComunicacion, request.toString());
        solicitudService.agregarMensajeParaPropuestaComunicacion(request, idSolicitud, idPropuestaComunicacion);
        log.info("<< Mensaje añadido para idPropuestaComunicacion {}", idPropuestaComunicacion);
    }

    @GetMapping(path = "/solicitud/{idSolicitud}/comunicarPropuesta", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public List<PropuestaSolicitud> obtenerTodasLasComunicacionesDeSolicitud(@PathVariable(required = true) Long idSolicitud)  {
        log.info(">> Request obtener todas las comunicanes de propuesta: {}", idSolicitud);
        List<PropuestaSolicitud> propuestaSolicitudsList = solicitudService.obtenerTodasLasPropuestasComunicacion(idSolicitud);
        log.info("<< Cantidad de propuestas obtenidas: {} para idSolicitud: {}",
                propuestaSolicitudsList.size(),
                idSolicitud);
        return propuestaSolicitudsList;
    }

    @GetMapping(path = "/solicitud/{idSolicitud}/comunicarPropuesta/{idComunicacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public PropuestaSolicitud obtenerComunicacionesDeSolicitudXIdComunicacion(@PathVariable(required = true) Long idComunicacion) {
        log.info(">> Request obtener comunicacion de propuesta x IdComunicacion: {}", idComunicacion);
        PropuestaSolicitud propuestaSolicitud = solicitudService.obtenerPropuestasComunicacionXId(idComunicacion);
        log.info("<< Comunicacr creado");
        return propuestaSolicitud;
    }

//    @GetMapping(path = "/solicitudes", produces = json)
//    public ResponseEntity<List<Solicitud>> listSolicitudes(@ModelAttribute @RequestParam(required = false) RequestFilterSolicitudes request){
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Solicitud> criteriaQuery = cb.createQuery(Solicitud.class);
//        Root<Solicitud> root = criteriaQuery.from(Solicitud.class);
//
//        Predicate predicate = cb.conjunction();
//
//        if(request.getIdFundacion() == null)
//            predicate = cb.and(predicate,cb.equal(root.get("fundacion_id_fundacion"),request.getIdFundacion()));
//        if(request.getTipoProducto() == null){
//            predicate = cb.createQuery("SELECT * FROM Perfiles");
//        if(request.getIdPerfil() == null)
//            predicate = cb.and(predicate,cb.equal(root.get("fundacion_id_fundacion"),request.getIdFundacion()));
//
//
//        return ResponseEntity.ok(solicitudes);
//    }
//
//    private Perfil getPerfilByFundacion(long idFundacion){
//
//    }
}

