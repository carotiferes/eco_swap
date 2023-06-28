package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import msUsers.domain.entities.Fundacion;
import msUsers.domain.entities.Perfil;
import msUsers.domain.entities.Producto;
import msUsers.domain.entities.Solicitud;
import msUsers.domain.repositories.FundacionesRepository;
import msUsers.domain.repositories.SolicitudRepository;
import msUsers.domain.requests.RequestSolicitud;
import msUsers.domain.responses.ResponsePostEntityCreation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SolicitudController {
    private final SolicitudRepository solicitudRepository;
    private final FundacionesRepository fundacionesRepository;

    //private UserContextService userContextService;

    public SolicitudController(SolicitudRepository solicitudRepository, FundacionesRepository fundacionesRepository) {
        this.solicitudRepository = solicitudRepository;
        this.fundacionesRepository = fundacionesRepository;
    }

    private static final String json = "application/json";

    @PostMapping(path = "/solicitud", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> crearSolicitud(@Valid @RequestBody RequestSolicitud requestSolicitud){

        //final var perfil = this.userContextService.getPerfil();

        var fundacionOptional = this.fundacionesRepository.findById(requestSolicitud.getIdFundacion());
        Fundacion fundacion = fundacionOptional.orElseThrow(() -> new EntityNotFoundException("¡La fundación no existe!"));

        List<Producto> productos;

        Solicitud solicitud = new Solicitud();
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setActiva(true);
        solicitud.setDescripcion(requestSolicitud.getDescripcion());
        solicitud.setFundacion(fundacion);
        solicitud.setTitulo(requestSolicitud.getTitulo());
        solicitud.setImagen("imagen.jpg");

        productos = requestSolicitud.getProductos().stream()
            .map(reqProducto -> {
               Producto p = new Producto();
               p.setSolicitud(solicitud);
               p.setDescripcion(reqProducto.getDescripcion());
               p.setCantidadSolicitada(reqProducto.getCantidadRequerida());
               p.setTipoProducto(reqProducto.getTipoProducto());
               return p;
            }).collect(Collectors.toList());

        solicitud.setProductos(productos);

        this.solicitudRepository.save(solicitud);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setId(requestSolicitud.getIdFundacion());
        responsePostEntityCreation.setDescripcion("Solicitud creada.");

        return ResponseEntity.created(location).body(responsePostEntityCreation);
    }
}
