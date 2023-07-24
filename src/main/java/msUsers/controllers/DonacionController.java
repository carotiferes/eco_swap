package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.repositories.DonacionesRepository;
import msUsers.domain.repositories.ParticularRepository;
import msUsers.domain.requests.donaciones.RequestCambiarEstadoDonacion;
import msUsers.domain.responses.ResponseUpdateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class DonacionController {

    @Autowired
    DonacionesRepository donacionesRepository;

    @Autowired
    ParticularRepository particularRepository;

    private static final String json = "application/json";

    @PutMapping(path = "/colecta/{id_colecta}/donaciones/{id_donacion}", produces = json)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> cambiarEstadoDonacion(
            @PathVariable("id_donacion") Long idDonacion,
            @RequestBody RequestCambiarEstadoDonacion request) {

        log.info(">> Se va cambiar el estado de la donacion {} a {}", idDonacion, request.getNuevoEstado());
        final var donacion = this.donacionesRepository.findById(idDonacion).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrada la donacion: " + idDonacion));

        EstadoDonacion anteriorEstado = donacion.getEstadoDonacion();

        try {
            EstadoDonacion nuevoEstado = EstadoDonacion.valueOf(request.getNuevoEstado());
        } catch (IllegalArgumentException  e) {
            log.error(request.getNuevoEstado() + " no es un estado valido");
            ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
            responseUpdateEntity.setStatus(HttpStatus.BAD_REQUEST.name());
            responseUpdateEntity.setDescripcion(request.getNuevoEstado() + " no es un estado valido");
            return ResponseEntity.badRequest().body(responseUpdateEntity);
        }
            EstadoDonacion nuevoEstado = EstadoDonacion.valueOf(request.getNuevoEstado());
            donacion.setEstadoDonacion(nuevoEstado);

            this.donacionesRepository.save(donacion);

            ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
            responseUpdateEntity.setStatus(HttpStatus.OK.name());
            responseUpdateEntity.setDescripcion("Se cambió el estado de la donación de " + anteriorEstado + " a " + request.getNuevoEstado());
            return ResponseEntity.ok(responseUpdateEntity);

        }

        @GetMapping(path = "/{id_particular}/donaciones", produces = json)
        public ResponseEntity<List<Donacion>> listarDonaciones(@PathVariable("id_particular") Long idParticular){
            final var particular = this.particularRepository.findById(idParticular).
                    orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular: " + idParticular));
            return ResponseEntity.ok(particular.getDonaciones());
        }
    }

