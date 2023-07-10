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
import msUsers.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import msUsers.domain.responses.ResponseSolicitudesList;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
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

    //private UserContextService userContextService;

    private static final String json = "application/json";

    @PostMapping(path = "/solicitud", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createSolicitud(@Valid @RequestBody RequestSolicitud requestSolicitud) {

        //final var perfil = this.userContextService.getPerfil();

        var fundacionOptional = this.fundacionesRepository.findById(requestSolicitud.getIdFundacion());
        Fundacion fundacion = fundacionOptional.orElseThrow(() -> new EntityNotFoundException("¡La fundacion no existe!"));

        Solicitud solicitud = new Solicitud();
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setActiva(true);
        solicitud.setDescripcion(requestSolicitud.getDescripcion());
        solicitud.setFundacion(fundacion);
        solicitud.setTitulo(requestSolicitud.getTitulo());

        String img = requestSolicitud.getImagen();
        /*
        String[] parts = img.split(",");
        if (parts.length > 1) {
            img = parts[1];
        }



        byte[] imgBytes = Base64.getDecoder().decode(img);
        String imageName = UUID.randomUUID().toString() + ".jpeg";
        String directorioActual = System.getProperty("user.dir");
        String storagePath = directorioActual + "/imagenes/" + imageName;

        File directory = new File(directorioActual + "/imagenes");
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directorio de almacenamiento creado correctamente.");
            }
        }

        try (OutputStream outputStream = new FileOutputStream(storagePath)) {
            outputStream.write(imgBytes);
        } catch (IOException e) {
            // Manejar el error de escritura de la imagen
            e.printStackTrace();
        }

        solicitud.setImagen(imageName);

         */
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

    @PostMapping(path = "/solicitud/{idSolicitud}/propuestas", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void crearComunicacionDePropuesta(
            @PathVariable(required = true) Long idSolicitud,
            @Valid @RequestBody RequestComunicarPropuestaSolicitudModel request) {
        log.info(">> Request para solicitud ID {} comunicacr propuesta: {}", idSolicitud, request.toString());
        solicitudService.crearPropuestaComunicacion(request, idSolicitud);
        log.info("<< Comunicacr creado");
    }

    /* POSTERGAMOS ESTE DESARROLLO
    @PutMapping(path = "/solicitud/{idSolicitud}/propuestas/{idPropuestaComunicacion}", consumes = json, produces = json)
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

    @GetMapping(path = "/solicitudes", produces = json)
    public ResponseEntity<List<ResponseSolicitudesList>> listSolicitudes(@ModelAttribute RequestFilterSolicitudes request) {
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
        if (solicitudes.isEmpty())
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

    @GetMapping(path = "/solicitud/{id_solicitud}", produces = json)
    public ResponseEntity<Solicitud> getSolicitud(@PathVariable("id_solicitud") Long id) {
        final var solicitud = this.solicitudRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado la solicitud: " + id));
        return ResponseEntity.ok(solicitud);
    }

    @GetMapping(path = "/getImage/{img}")
    public ResponseEntity<Resource> getImage(@PathVariable("img") String img){
        String dir = System.getProperty("user.dir") + "\\imagenes\\";
        Resource imagenResource = new FileSystemResource(dir + img);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagenResource);
    }
}

