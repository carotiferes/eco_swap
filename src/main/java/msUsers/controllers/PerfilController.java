package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import msUsers.domain.entities.Perfil;
import msUsers.domain.repositories.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class PerfilController {

    private PerfilRepository perfilRepository;

    public PerfilController(PerfilRepository perfilRepository) {this.perfilRepository = perfilRepository;}
    @GetMapping(path = "/perfil/{id_perfil}", produces = "application/json")
    @Transactional(readOnly = true)
    public ResponseEntity<Perfil> getPerfilById(@PathVariable("id_perfil") Long id){
        final var perfil = this.perfilRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(new Exception("No fue encontrado el perfil: " + id)));
        return ResponseEntity.ok(perfil);
    }

}
