package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.CaracteristicaProducto;
import msUsers.domain.entities.Particular;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.enums.EstadoPublicacion;
import msUsers.domain.entities.enums.TipoPublicacion;
import msUsers.domain.repositories.ParticularRepository;
import msUsers.domain.repositories.PublicacionRepository;
import msUsers.domain.requests.RequestPublicacion;
import msUsers.domain.responses.ResponsePostEntityCreation;
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

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class PublicacionController {

    @Autowired
    PublicacionRepository publicacionRepository;
    @Autowired
    ParticularRepository particularRepository;
    @Autowired
    ImageService imageService;
    private static final String json = "application/json";

    @PostMapping(path = "/publicacion", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> createPublicacion(@Valid @RequestBody RequestPublicacion requestPublicacion) {

        //final var perfil = this.userContextService.getUsuario();
        log.info(">> Request de creación de la publicación: {}", requestPublicacion.getTitulo());

        var particularOptional = this.particularRepository.findById(requestPublicacion.getIdParticular());
        Particular particular = particularOptional.orElseThrow(() -> new EntityNotFoundException("¡El particular no existe!"));

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(requestPublicacion.getTitulo());
        publicacion.setEstadoPublicacion(EstadoPublicacion.PENDIENTE);
        publicacion.setParticular(particular);
        publicacion.setValorTruequeMax(requestPublicacion.getValorTruequeMax());
        publicacion.setValorTruequeMin(requestPublicacion.getValorTruequeMin());
        publicacion.setDescripcion(requestPublicacion.getDescripcion());

        List<CaracteristicaProducto> listaCaracteristicas = requestPublicacion.getCaracteristicasProducto()
                .stream()
                .map(s -> CaracteristicaProducto.armarCarateristica(s, requestPublicacion.getIdParticular()))
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
        var entity = this.publicacionRepository.save(publicacion); // Actualizo la publicacion con la imagen

        log.info(">> Publicación creada con ID: {}", entity.getIdPublicacion());

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        ResponsePostEntityCreation responsePostEntityCreation = new ResponsePostEntityCreation();
        responsePostEntityCreation.setId(entity.getIdPublicacion());
        responsePostEntityCreation.setDescripcion("Publicacion creada.");
        responsePostEntityCreation.setStatus(HttpStatus.OK.name());
        log.info("<< Publicacion creada con ID: {}", entity.getIdPublicacion());

        return ResponseEntity.created(location).body(responsePostEntityCreation);

    }
}
