package msAutenticacion.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Direccion;
import msAutenticacion.domain.entities.Fundacion;
import msAutenticacion.domain.entities.Particular;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.entities.enums.TipoDocumento;
import msAutenticacion.domain.model.EnumValue;
import msAutenticacion.domain.model.UsuarioContext;
import msAutenticacion.domain.model.enums.TipoDocumentoEnum;
import msAutenticacion.domain.requests.*;
import msAutenticacion.domain.responses.DTOs.TipoDocumentoDTO;
import msAutenticacion.domain.responses.DTOs.UsuarioDTO;
import msAutenticacion.domain.responses.ResponseLogin;
import msAutenticacion.domain.responses.ResponseUpdateEntity;
import msAutenticacion.exceptions.LoginUserBlockedException;
import msAutenticacion.exceptions.LoginUserException;
import msAutenticacion.exceptions.LoginUserWrongCredentialsException;
import msAutenticacion.services.CriteriaBuilderQueries;
import msAutenticacion.services.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;
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

    @PostMapping(path = "/usuario/signup", consumes = JSON, produces = JSON)
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> postSignUp(@RequestBody @Valid RequestSignUp body){
        try {
            log.info("postSignUp: creando nuevo usuario: " + body.getEmail());
            Usuario usuarioCreado = usuarioService.crearUsuario(body);
            log.info("postSignUp: Usuario creado con ID: "+ usuarioCreado.getIdUsuario());
            return ResponseEntity.ok(usuarioCreado.getIdUsuario());
        } catch (Exception e) {
            log.error("postSignUp: ERROR AL CREAR USER: " + e.getMessage());
            throw e;
        }
    }

    @PatchMapping(path = "/usuario/password", produces = JSON)
    @Transactional
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ResponseUpdateEntity> putPassword(@RequestBody RequestPassword body){
        final Usuario user = UsuarioContext.getUsuario();
        log.info("putPassword: Actualizar contraseña para usuario: "+ user.getIdUsuario());
        usuarioService.actualizarContrasenia(body, user.getIdUsuario());
        log.info("putPassword: Contraseña actualizada para usuario: "+ user.getIdUsuario());
        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        responseUpdateEntity.setDescripcion("Contraseña cambiada exitosamente.");
        responseUpdateEntity.setStatus(HttpStatus.OK.name());
        return ResponseEntity.ok(responseUpdateEntity);
    }

    @PatchMapping(path = "/usuario/login", produces = JSON)
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TipoDocumentoDTO>> getTipoDocumentos() {
        log.info(">> Ingresando a getTipoDocumentos");
        List<TipoDocumentoDTO> tiposDocumento = Arrays.stream(TipoDocumentoEnum.values()).
                map(tipoDocumento -> new TipoDocumentoDTO(tipoDocumento.name(),obtenerDescripcion(tipoDocumento)))
                .collect(Collectors.toList());
        log.info("<< Retornando listado de tipoDocumentos: {}", tiposDocumento);
        return ResponseEntity.ok(tiposDocumento);
    }

    @PutMapping(path = "/usuario/edit", consumes = JSON, produces = JSON)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> editProfile(@RequestBody @Valid RequestEditProfile requestEditProfile){
        log.info("Request: {}", requestEditProfile);
        final Usuario user = UsuarioContext.getUsuario();
        log.info("Editar perfil >> Editando el perfil: {}", user.getEmail());
        ResponseUpdateEntity responseUpdateEntity = usuarioService.editarUsuario(requestEditProfile, user);
        return ResponseEntity.ok(responseUpdateEntity);

    }

    @PatchMapping(path = "/usuario/confirmar", consumes = JSON, produces = JSON)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> confirmarProfile(@RequestBody @Valid RequestConfirm requestConfirm){
        log.info(">> Confirmación de usuario: {}", requestConfirm.getIdUsuario());
        Boolean confirmado = usuarioService.confirmarUsuario(requestConfirm);
        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        responseUpdateEntity.setDescripcion("Validado: " + confirmado.toString().toUpperCase());
        responseUpdateEntity.setStatus(HttpStatus.OK.name());
        log.info("<< usuario confirmado: {}", requestConfirm.getIdUsuario());
        return ResponseEntity.ok(responseUpdateEntity);
    }

    @PatchMapping(path = "/usuario/reenvio/{id_usuario}", consumes = JSON, produces = JSON)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ResponseUpdateEntity> confirmarProfile(@PathVariable(name = "id_usuario") Long idUsuario){
        log.info(">> Reenvio de email con nuevo token al usuario: {}", idUsuario);
        usuarioService.reenviarCodigoConfirmacion(idUsuario);
        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        responseUpdateEntity.setDescripcion("Nuevo código de confirmación generado con éxito y email enviado.");
        responseUpdateEntity.setStatus(HttpStatus.OK.name());
        return ResponseEntity.ok(responseUpdateEntity);
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
