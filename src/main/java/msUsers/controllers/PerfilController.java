package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import msUsers.domain.entities.Perfil;
import msUsers.domain.repositories.PerfilRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class PerfilController {

    private final PerfilRepository perfilRepository;
    private static final String json = "application/json";

    public PerfilController(PerfilRepository perfilRepository) {this.perfilRepository = perfilRepository;}
    @GetMapping(path = "/perfil/{id_perfil}", produces = json)
    @Transactional(readOnly = true)
    public ResponseEntity<Perfil> getPerfilById(@PathVariable("id_perfil") Long id){
        final var perfil = this.perfilRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(new Exception("No fue encontrado el perfil: " + id)));
        System.out.println("\u001B[32m" + "Encontrado perfil: " + perfil.getUsername());
        return ResponseEntity.ok(perfil);
    }

}
