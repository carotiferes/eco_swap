package msUsers.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoDonacion;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.DonacionesRepository;
import msUsers.domain.repositories.ParticularRepository;
import msUsers.domain.requests.donaciones.RequestCambiarEstadoDonacion;
import msUsers.domain.requests.donaciones.RequestComunicarDonacionColectaModel;
import msUsers.domain.responses.DTOs.ColectaDTO;
import msUsers.domain.responses.DTOs.DonacionDTO;
import msUsers.domain.responses.ResponsePostEntityCreation;
import msUsers.domain.responses.ResponseUpdateEntity;
import msUsers.services.ColectaService;
import msUsers.services.CriteriaBuilderQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class DonacionController {

    @Autowired
    DonacionesRepository donacionesRepository;
    @Autowired
    ColectaService colectaService;
    @Autowired
    EntityManager entityManager;
    @Autowired
    CriteriaBuilderQueries criteriaBuilderQueries;
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
            responseUpdateEntity.setDescripcion("Se cambio el estado de la donacion de " + anteriorEstado + " a " + request.getNuevoEstado());
            return ResponseEntity.ok(responseUpdateEntity);

        }

    @GetMapping(path = "/particular/misDonaciones", produces = json)
    public ResponseEntity<List<DonacionDTO>> listarDonacionesPorParticular(){

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular."));

        List<DonacionDTO> donacionesDTO = particular.getDonaciones().stream().map(donacion -> {
            DonacionDTO donacionDTO = new DonacionDTO();
            donacionDTO.setCantidadDonacion(donacion.getCantidadDonacion());
            donacionDTO.setCaracteristicaDonacion(donacion.getCaracteristicaDonacion());
            donacionDTO.setEstadoDonacion(donacion.getEstadoDonacion());
            donacionDTO.setImagenes(donacion.getImagenes());
            donacionDTO.setIdParticular(donacion.getParticular().getIdParticular());
            donacionDTO.setDescripcion(donacion.getDescripcion());
            donacionDTO.setNombreParticular(donacion.getParticular().getNombre());
            donacionDTO.setProducto(donacion.getProducto().toDTO());
            return donacionDTO;
        }).toList();
        return ResponseEntity.ok(donacionesDTO);
    }

    @PostMapping(path = "/colecta/{id_colecta}/crearDonacion", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<ResponsePostEntityCreation> crearDonacionEnColecta(
            @PathVariable(required = true, name = "id_colecta") Long idColecta,
            @Valid @RequestBody RequestComunicarDonacionColectaModel request){
        log.info(">> Request para colecta ID {} creacion donacion: {}", idColecta, request.toString());

        final Usuario user = UsuarioContext.getUsuario();
        Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
        Particular particular = optionalParticular.orElseThrow(() -> new EntityNotFoundException("No fue encontrado el particular."));

        colectaService.crearDonacion(request, idColecta, particular.getIdParticular());
        ResponsePostEntityCreation response = new ResponsePostEntityCreation();
        response.setId(idColecta);
        response.setDescripcion("Donacion creada.");
        response.setStatus(HttpStatus.OK.name());
        log.info("<< Donacion creada en la colecta: {}", idColecta);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/colecta/{id_colecta}/donaciones", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public List<Donacion> obtenerTodasLasDonacionesDeColecta(@PathVariable(required = true, name = "id_colecta") Long idColecta)  {
        log.info(">> Request obtener todas las donaciones de la colecta: {}", idColecta);
        List<Donacion> donacionColectesList = colectaService.obtenerTodasLasDonaciones(idColecta);
        log.info("<< Cantidad de donaciones obtenidas: {} para idColecta: {}",
                donacionColectesList.size(), idColecta);
        return donacionColectesList;
    }

    @GetMapping(path = "/colecta/{id_colecta}/donaciones/{id_donacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.OK)
    public Donacion obtenerDonacionesXIdDonacion(@PathVariable(required = true, name = "id_donacion") Long idDonacion) {
        log.info(">> Request obtener donacion x idDonacion: {}", idDonacion);
        Donacion donacion = colectaService.obtenerDonacionXIdDonacion(idDonacion);
        log.info("<< Donacion obtenido: {}", donacion);
        return donacion;
    }


    /*
    COMUNICACION DE DONACIONES
     */

    /* POSTERGAMOS ESTE DESARROLLO
    @PutMapping(path = "/colecta/{idColecta}/donaciones/{idDonacionComunicacion}", consumes = json, produces = json)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Object> agregarMensajeComunicacionDeDonacion(
            @PathVariable(required = true) Long idColecta,
            @PathVariable(required = true) Long idDonacionComunicacion,
            @Valid @RequestBody RequestMensajeRespuesta request) {
        log.info(">> Request para idDonacionComunicacion {} con mensaje para comunicar donacion: {}",
                idDonacionComunicacion, request.toString());
        colectaService.agregarMensajeParaDonacionComunicacion(request, idColecta, idDonacionComunicacion);
        log.info("<< Mensaje añadido para idDonacionComunicacion {}", idDonacionComunicacion);
        return ResponseEntity.ok().build();
    }
     */
}

