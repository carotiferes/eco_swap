package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.responses.DTOs.FundacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class HealthController {

    @GetMapping(path = "/health")
    public ResponseEntity<String> healthCheck(){
        log.info(">> Se solciita estado del servicio MS-USUARIO con estado OK");
        return ResponseEntity.ok("OK");
    }
}
