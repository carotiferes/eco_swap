package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Notificacion;
import msUsers.domain.entities.Usuario;
import msUsers.domain.entities.enums.EstadoNotificacion;
import msUsers.domain.model.UsuarioContext;
import msUsers.domain.repositories.NotificacionesRepository;
import msUsers.domain.requests.RequestNotificaciones;
import msUsers.domain.responses.DTOs.NotificacionDTO;
import msUsers.domain.responses.ResponseUpdateEntity;
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
import java.util.stream.Collectors;

@RestController
@Slf4j
@Validated
@RequestMapping("/api")
public class NotificacionController {

    @Autowired
    private NotificacionesRepository notificacionesRepository;
    @Autowired
    private CriteriaBuilderQueries criteriaBuilderQueries;
    private static final String json = "application/json";

    @GetMapping(path = "/misNotificaciones", produces = json)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NotificacionDTO>> getMisNotificaciones() {
        final Usuario user = UsuarioContext.getUsuario();
        List<NotificacionDTO> notificacionesDTOS = user.getNotificaciones().stream().map(Notificacion::toDTO).collect(Collectors.toList());
        log.info("El usuario {} tiene {} notificaciones", user.getEmail(), user.getNotificaciones().size());
        return ResponseEntity.ok(notificacionesDTOS);
    }

    @PutMapping(path = "/notificacion", produces = json)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> marcarNotificacionComoLeida(@RequestBody RequestNotificaciones requestNotificaciones) {
        log.info(">> Se va a cambiar el estado de {} notificaciones a {}", requestNotificaciones.getIdNotificaciones().size(), EstadoNotificacion.LEIDO);

        List<Long> notificacionesNoEncontradas = new ArrayList<>();

        for (Long idNotificacion : requestNotificaciones.getIdNotificaciones()) {
            Optional<Notificacion> notificacionOptional = notificacionesRepository.findById(idNotificacion);
            if (notificacionOptional.isPresent()) {
                Notificacion notificacion = notificacionOptional.get();
                notificacion.setEstadoNotificacion(EstadoNotificacion.LEIDO);
                notificacionesRepository.save(notificacion);
            } else {
                notificacionesNoEncontradas.add(idNotificacion);
            }
        }

        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        if (notificacionesNoEncontradas.isEmpty()) {
            responseUpdateEntity.setStatus(HttpStatus.OK.name());
            responseUpdateEntity.setDescripcion("Se marcaron como leídas todas las notificaciones.");
        } else {
            responseUpdateEntity.setStatus(HttpStatus.PARTIAL_CONTENT.name());
            responseUpdateEntity.setDescripcion("Algunas notificaciones no fueron encontradas: " + notificacionesNoEncontradas);
        }

        return ResponseEntity.ok(responseUpdateEntity);
    }
}
