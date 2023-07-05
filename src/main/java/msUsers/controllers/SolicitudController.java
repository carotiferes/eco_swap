package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import msUsers.domain.entities.*;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.repositories.PerfilRepository;
import msUsers.domain.repositories.PropuestasRepository;
import msUsers.domain.repositories.SolicitudRepository;
import msUsers.domain.requests.RequestFilterSolicitudes;
import msUsers.domain.requests.RequestSolicitud;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.domain.responses.ResponseSolicitudesList;
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
@RequestMapping("/api")
public class SolicitudController {
    private final SolicitudRepository solicitudRepository;
    private final FundacionesRepository fundacionesRepository;
    private final PropuestasRepository propuestasRepository;
    private final EntityManager entityManager;

    private final PerfilRepository perfilRepository;

    //private UserContextService userContextService;

    public SolicitudController(SolicitudRepository solicitudRepository, FundacionesRepository fundacionesRepository, PropuestasRepository propuestasRepository, EntityManager entityManager, PerfilRepository perfilRepository) {
        this.solicitudRepository = solicitudRepository;
        this.fundacionesRepository = fundacionesRepository;
        this.propuestasRepository = propuestasRepository;
        this.entityManager = entityManager;
        this.perfilRepository = perfilRepository;
    }

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

    @GetMapping(path = "/solicitudes", produces = json)
    public ResponseEntity<List<ResponseSolicitudesList>> listSolicitudes(@ModelAttribute RequestFilterSolicitudes request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Solicitud> query = cb.createQuery(Solicitud.class);
        Root<Solicitud> from = query.from(Solicitud.class);
        Predicate predicate = cb.conjunction();

        if (request.getIdFundacion() != null) {
            Join<Solicitud, Fundacion> join = from.join("fundacion");
            predicate = cb.and(predicate, cb.equal(join.get("idFundacion"),request.getIdFundacion()));
        }
        if (request.getCodigoPostal() != null) {
            Join<Solicitud, Fundacion> fundacionJoin = from.join("fundacion");
            Join<Fundacion, Perfil> perfilJoin = fundacionJoin.join("perfil");
            Join<Perfil, Direccion> direccionJoin = perfilJoin.join("direcciones");
            predicate = cb.and(predicate, cb.equal(direccionJoin.get("codigoPostal"),request.getCodigoPostal()));
        }

        if (request.getTipoProducto() != null) {
            Join<Solicitud, Producto> join = from.join("productos");
            predicate = cb.and(predicate, cb.equal(join.get("tipoProducto"),request.getTipoProducto()));
        }

        query.where(predicate);

        List<Solicitud> solicitudes = entityManager.createQuery(query).getResultList();
        if(solicitudes.isEmpty())
            throw new EntityNotFoundException("No existen fundaciones con estos criterios. Intente otra búsqueda.");

        List<ResponseSolicitudesList> solicitudesDTO = solicitudes.stream().map(solicitud -> {
            ResponseSolicitudesList responseSolicitudesList = new ResponseSolicitudesList();
            responseSolicitudesList.setTituloSolicitud(solicitud.getTitulo());
            responseSolicitudesList.setFundacion(solicitud.getFundacion().getNombre());
            responseSolicitudesList.setProductos(solicitud.getProductos());
            responseSolicitudesList.setIdSolicitud(solicitud.getIdSolicitud());
            responseSolicitudesList.setImagen(solicitud.getImagen());
            return responseSolicitudesList;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(solicitudesDTO);
    }
}

