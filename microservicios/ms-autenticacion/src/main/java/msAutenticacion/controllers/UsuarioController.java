package msAutenticacion.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.UsuarioRepository;
import msAutenticacion.domain.requests.RequestLogin;
import msAutenticacion.domain.requests.RequestPassword;
import msAutenticacion.domain.requests.RequestSignin;
import msAutenticacion.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;
    private static final String json = "application/json";

    public UsuarioController(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
    @GetMapping(path = "/usuario/{id_usuario}", produces = json)
    @Transactional(readOnly = true)
    public ResponseEntity<Usuario> getUserById(@PathVariable("id_usuario") Long id){
        log.info("getUserById: obtener Usuario a partir de ID: "+ id);
        final var usuario = usuarioService.obtenerUsuario(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + id));
        log.info("getUserById: usuario obtenido para UserId: "+ id);
        usuario.setPassword("");
        usuario.setSalt("");
        return ResponseEntity.ok(usuario);
    }

    @PostMapping(path = "/usuario/signin", produces = json)
    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Long> postSignin(@RequestBody RequestSignin body){
        log.info("postSignin: creando nuevo usuario: "+ body.getEmail());
        Long userId = usuarioService.crearUsuario(body);
        log.info("postSignin: Usuario creado con ID: "+ userId);
        return ResponseEntity.ok(userId);
    }

    @PutMapping(path = "/usuario/password", produces = json)
    @Transactional(readOnly = true)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<HttpStatus> putPassword(@RequestBody RequestPassword body){
        log.info("putPassword: Actualizar contraseña para usuario: "+ body.getUsername());
        usuarioService.actualizarContrasenia(body);
        log.info("putPassword: Contraseña actualizada para usuario: "+ body.getUsername());
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/usuario/login", produces = json)
    @Transactional(readOnly = true)
    public ResponseEntity<Boolean> postLogin(@RequestBody RequestLogin body){
        log.info("postLogin: Intento de login para usuario: "+ body.getUsername());
        Boolean resultadoLogin = usuarioService.login(body);
        log.info("postLogin: Resultado del Login para usuario: "+ body.getUsername() + " con resultado: " + resultadoLogin);
        return ResponseEntity.ok(resultadoLogin);
    }

}
