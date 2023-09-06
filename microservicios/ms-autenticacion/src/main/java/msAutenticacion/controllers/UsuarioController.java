package msAutenticacion.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Fundacion;
import msAutenticacion.domain.entities.Particular;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.model.EnumValue;
import msAutenticacion.domain.model.enums.TipoDocumentoEnum;
import msAutenticacion.domain.requests.RequestLogin;
import msAutenticacion.domain.requests.RequestPassword;
import msAutenticacion.domain.requests.RequestSignUp;
import msAutenticacion.domain.responses.DTOs.TipoDocumentoDTO;
import msAutenticacion.domain.responses.DTOs.UsuarioDTO;
import msAutenticacion.domain.responses.ResponseLogin;
import msAutenticacion.exceptions.LoginUserBlockedException;
import msAutenticacion.exceptions.LoginUserException;
import msAutenticacion.exceptions.LoginUserWrongCredentialsException;
import msAutenticacion.services.CriteriaBuilderQueries;
import msAutenticacion.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ms-autenticacion/api/v1")
@Slf4j
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CriteriaBuilderQueries criteriaBuilderQueries;

    private static final String JSON = "application/json";

    @GetMapping(path = "/usuario/{id_usuario}", produces = JSON)
    @Transactional(readOnly = true)
    public ResponseEntity<UsuarioDTO> getUserById(@PathVariable("id_usuario") Long id){
        log.info("getUserById: obtener Usuario a partir de ID: "+ id);
        final var usuario = usuarioService.obtenerUsuario(id).
                orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + id));
        log.info("getUserById: usuario obtenido para UserId: "+ id);

        UsuarioDTO usuarioDTO = usuario.toDTO();

        if(usuario.isSwapper()) {
            Optional<Particular> particular = criteriaBuilderQueries.getParticularPorUsuario(usuario.getIdUsuario());
            usuarioDTO.setParticularDTO(particular.get().toDTO());
        } else {
            Optional<Fundacion> fundacion = criteriaBuilderQueries.getFundacionPorUsuario(usuario.getIdUsuario());
            usuarioDTO.setFundacionDTO(fundacion.get().toDTO());
        }

        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping(path = "/usuario/signup", produces = JSON)
    @Transactional
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Long> postSignUp(@RequestBody RequestSignUp body){
        log.info("postSignUp: creando nuevo usuario: "+ body.getEmail());
        Long userId = usuarioService.crearUsuario(body);
        log.info("postSignUp: Usuario creado con ID: "+ userId);
        return ResponseEntity.ok(userId);
    }

    @PutMapping(path = "/usuario/password", produces = JSON)
    @Transactional
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<HttpStatus> putPassword(@RequestBody RequestPassword body){
        log.info("putPassword: Actualizar contraseña para usuario: "+ body.getUsername());
        usuarioService.actualizarContrasenia(body);
        log.info("putPassword: Contraseña actualizada para usuario: "+ body.getUsername());
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "/usuario/login", produces = JSON)
    @Transactional(noRollbackFor = {LoginUserException.class, LoginUserBlockedException.class, LoginUserWrongCredentialsException.class})
    public ResponseEntity<ResponseLogin> patchLogin(@RequestBody @Valid RequestLogin body) throws NoSuchAlgorithmException {
        log.info("postLogin: Intento de login para usuario: "+ body.getUsername());
        String jwt = usuarioService.login(body);
        log.info("postLogin: Resultado del Login para usuario: "+ body.getUsername() + " con resultado: " + jwt);
        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setToken(jwt);
        return ResponseEntity.ok(responseLogin);
    }

    @GetMapping(path = "/tiposDocumentos", produces = JSON)
    public ResponseEntity<List<TipoDocumentoDTO>> getTipoDocumentos() {
        log.info(">> Ingresando a getTipoDocumentos");
        List<TipoDocumentoDTO> tiposDocumento = Arrays.stream(TipoDocumentoEnum.values()).
                map(tipoDocumento -> new TipoDocumentoDTO(tipoDocumento.name(),obtenerDescripcion(tipoDocumento)))
                .collect(Collectors.toList());
        log.info("<< Retornando listado de tipoDocumentos: {}", tiposDocumento);
        return ResponseEntity.ok(tiposDocumento);
    }


    private String obtenerDescripcion(TipoDocumentoEnum tipoProducto) {
        try {
            EnumValue annotation = tipoProducto.getClass()
                    .getField(tipoProducto.name())
                    .getAnnotation(EnumValue.class);
            return annotation != null ? annotation.description() : "";
        } catch (NoSuchFieldException e) {
            return "";
        }
    }

}
