package msUsers.controllers;

import jakarta.persistence.EntityNotFoundException;
import msUsers.domain.entities.Usuario;
import msUsers.domain.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private static final String json = "application/json";

    public UsuarioController(UsuarioRepository usuarioRepository) {this.usuarioRepository = usuarioRepository;}
    @GetMapping(path = "/usuario/{id_usuario}", produces = json)
    @Transactional(readOnly = true)
    public ResponseEntity<Usuario> getUserById(@PathVariable("id_usuario") Long id){
        final var perfil = this.usuarioRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + id));
        return ResponseEntity.ok(perfil);
    }


}
