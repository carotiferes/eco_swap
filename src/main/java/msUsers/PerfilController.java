package msUsers;

import domain.entities.Perfil;
import domain.errors.ErrorMessageConstants;
import domain.repositories.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "users")
public class PerfilController {

    private PerfilRepository perfilRepository;

    @Autowired
    public PerfilController(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @GetMapping(path = "users/{id}",produces = "application/json")
    @Transactional(readOnly = true)
    public ResponseEntity<Perfil> getPerfilById(@PathVariable("idPerfil") UUID id){
        final var perfil = this.perfilRepository.findById(id).
                                                orElseThrow(() -> new EntityNotFoundException(ErrorMessageConstants.PERFIL_NOT_FOUND));

        return ResponseEntity.ok(perfil);

        //ToDo: Test.
    }
}
