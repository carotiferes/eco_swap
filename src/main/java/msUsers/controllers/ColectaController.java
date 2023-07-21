package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.criteria.*;
import msUsers.domain.entities.*;
import msUsers.domain.repositories.ColectaRepository;
import msUsers.domain.repositories.DonacionesRepository;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.repositories.UsuarioRepository;
import msUsers.domain.requests.RequestFilterColectas;
import msUsers.domain.requests.donaciones.RequestComunicarDonacionColectaModel;
import msUsers.domain.requests.RequestColecta;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.services.ColectaService;
import msUsers.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import msUsers.domain.responses.ResponseColectasList;
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
public class ColectaController {
    @Autowired
    ColectaRepository colectaRepository;
    @Autowired FundacionesRepository fundacionesRepository;
    @Autowired
    DonacionesRepository donacionesRepository;
    @Autowired EntityManager entityManager;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ColectaService colectaService;
    @Autowired
    ImageService imageService;

    //private UserContextService userContextService;

    private static final String json = "application/json";


    @PostMapping(path = "/colecta", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createSolicitud(@Valid @RequestBody RequestColecta requestColecta) {

        //final var perfil = this.userContextService.getUsuario();
        log.info(">> Request de creación de colecta: {}", requestColecta.getTitulo());

        var fundacionOptional = this.fundacionesRepository.findById(requestColecta.getIdFundacion());
        Fundacion fundacion = fundacionOptional.orElseThrow(() -> new EntityNotFoundException("¡La fundacion no existe!"));

        Colecta colecta = new Colecta();
        colecta.setFechaColecta(LocalDate.now());
        colecta.setActiva(true);
        colecta.setDescripcion(requestColecta.getDescripcion());
        colecta.setFundacion(fundacion);
        colecta.setTitulo(requestColecta.getTitulo());

        // ToDo: En el caso de que la colecta falle por alguna razón, la imagen se sigue guardando. CORREGIR.

        String img = requestColecta.getImagen();
        colecta.setImagen(imageService.saveImage(img));

        List<Producto> productos = requestColecta.getProductos().stream()
                .map(reqProducto -> {
                    Producto p = new Producto();
                    p.setColecta(colecta);
                    p.setDescripcion(reqProducto.getDescripcion());
                    p.setCantidadSolicitada(reqProducto.getCantidadRequerida());
                    p.setTipoProducto(reqProducto.getTipoProducto());
                    return p;
                }).collect(Collectors.toList());

        colecta.setProductos(productos);

        // Guardo la colecta y me quedo con el id generada en la base de datos
        var entity = this.colectaRepository.save(colecta);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setId(entity.getIdColecta());
        responsePostEntityCreation.setDescripcion("Colecta creada.");
        responsePostEntityCreation.setStatus(HttpStatus.OK.name());
        log.info("<< Colecta creada con ID: {}", entity.getIdColecta());

        return ResponseEntity.created(location).body(responsePostEntityCreation);
    }

    /*
    COMUNICACION DE DONACIONES
     */

    @PostMapping(path = "/colecta/{id_colecta}/donaciones", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> crearComunicacionDeDonacion(
            @PathVariable(required = true) Long idColecta,
            @Valid @RequestBody RequestComunicarDonacionColectaModel request){
        log.info(">> Request para colecta ID {} comunicar donacion: {}", idColecta, request.toString());
        colectaService.crearPropuestaComunicacion(request, idColecta);
        ResponsePostEntityCreation response = new ResponsePostEntityCreation();
        response.setId(idColecta);
        response.setDescripcion("Comunicación creada.");
        response.setStatus(HttpStatus.OK.name());
        log.info("<< Comunicacion creada en la solicitud: {}", idColecta);
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
        colectaService.agregarMensajeParaPropuestaComunicacion(request, idSolicitud, idPropuestaComunicacion);
        log.info("<< Mensaje añadido para idPropuestaComunicacion {}", idPropuestaComunicacion);
        return ResponseEntity.ok().build();
    }
     */

    @GetMapping(path = "/colecta/{id_colecta}/donaciones", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public List<Donacion> obtenerTodasLasComunicacionesDeColecta(@PathVariable(required = true) Long idColecta)  {
        log.info(">> Request obtener todas las comunicaciones de donacion: {}", idColecta);
        List<Donacion> donacionSolicitudesList = colectaService.obtenerTodasLasPropuestasComunicacion(idColecta);
        log.info("<< Cantidad de donaciones obtenidas: {} para idColecta: {}",
                donacionSolicitudesList.size(),
                idColecta);
        return donacionSolicitudesList;
    }

    @GetMapping(path = "/colecta/{id_colecta}/donaciones/{idDonacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public Donacion obtenerComunicacionesDeColectaXIdDonacion(@PathVariable(required = true) Long idDonacion) {
        log.info(">> Request obtener comunicacion de donacion x idDonacion: {}", idDonacion);
        Donacion donacionSolicitud = colectaService.obtenerPropuestasComunicacionXId(idDonacion);
        log.info("<< Donacion obtenido: {}", donacionSolicitud);
        return donacionSolicitud;
    }

    @GetMapping(path = "/colectas", produces = json)
    public ResponseEntity<List<ResponseColectasList>> listColectas(@ModelAttribute RequestFilterColectas request) {
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
            Join<Fundacion, Usuario> perfilJoin = fundacionJoin.join("perfil");
            Join<Usuario, Direccion> direccionJoin = perfilJoin.join("direcciones");
            predicate = cb.and(predicate, cb.equal(direccionJoin.get("codigoPostal"), request.getCodigoPostal()));
        }

        if (request.getTipoProducto() != null) {
            Join<Colecta, Producto> join = from.join("productos");
            predicate = cb.and(predicate, cb.equal(join.get("tipoProducto"), request.getTipoProducto()));
        }

        query.where(predicate);

        List<Colecta> solicitudes = entityManager.createQuery(query).getResultList();

        List<ResponseColectasList> solicitudesDTO = solicitudes.stream().map(solicitud -> {
            ResponseColectasList responseColectasList = new ResponseColectasList();
            responseColectasList.setTitulo(solicitud.getTitulo());
            responseColectasList.setFundacion(solicitud.getFundacion().getNombre());
            responseColectasList.setProductos(solicitud.getProductos());
            responseColectasList.setIdColecta(solicitud.getIdColecta());
            responseColectasList.setImagen(solicitud.getImagen());
            responseColectasList.setIdFundacion(solicitud.getFundacion().getIdFundacion());
            return responseColectasList;
        }).collect(Collectors.toList());

        log.info("<< {} solicitudes encontradas.", solicitudesDTO.size());
        return ResponseEntity.ok(solicitudesDTO);
    }

    @GetMapping(path = "/colecta/{id_colecta}", produces = json)
    public ResponseEntity<Colecta> getColecta(@PathVariable("id_colecta") Long id) {
        final var solicitud = this.colectaRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la solicitud: " + id));
        return ResponseEntity.ok(solicitud);
    }

}

