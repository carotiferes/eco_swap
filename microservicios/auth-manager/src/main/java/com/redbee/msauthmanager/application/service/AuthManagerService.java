package com.redbee.msauthmanager.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mifmif.common.regex.Generex;
import com.redbee.msauthmanager.adapter.controller.model.UserLoginRequest;
import com.redbee.msauthmanager.adapter.controller.model.UserRestaurarPasswordRequest;
import com.redbee.msauthmanager.adapter.controller.model.UserSignInRequest;
import com.redbee.msauthmanager.adapter.database.UsuariosAdapter;
import com.redbee.msauthmanager.adapter.database.model.UsuarioModel;
import com.redbee.msauthmanager.application.exception.BusinessException;
import com.redbee.msauthmanager.config.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class AuthManagerService {

    @Autowired
    private UsuariosAdapter usuariosAdapter;

    private static final String PEPPER = "A5w2";

    public void crearUser(UserSignInRequest data) throws NoSuchAlgorithmException {
        log.info("Creacion de nuevo usuario: Email: {}; Username: {}", data.getEmail(), data.getUsername());
        UsuarioModel userXUsername = usuariosAdapter.obtenerUser(data.getUsername());
        UsuarioModel userXEmail = usuariosAdapter.obtenerUserXEmail(data.getUsername());
        if(userXUsername!=null || userXEmail!=null) {
            throw new BusinessException(ErrorCode.EMAIL_O_USER_YA_EXISTENTES);
        }
        String salt = this.createSalt();
        String newPassword = data.getPassword()+salt+PEPPER;
        String hashPassport = this.encriptarPassword(newPassword);
        UsuarioModel model = UsuarioModel.builder()
                .email(data.getEmail())
                .salt(salt)
                .username(data.getUsername())
                .password(hashPassport)
                .build();
        usuariosAdapter.guardarUsuario(model);
        log.info("Se ha creado usuario con Emaill: {}; Username: {}", data.getEmail(), data.getUsername());
        //AL FINALIZAR EL GUARDADO DEL NUEVO PERFIL, SE DEBE ENVIAR UN EMAIL CONFIRMANDO QUE SE GUARDO EL USUARIO
    }

    //DEVUELVE UN JWT TOKEN
    public String loginUser(UserLoginRequest data) throws Exception {
        log.info("Solicitud de Login para el Username: {}", data.getUsername());
        UsuarioModel model = usuariosAdapter.obtenerUser(data.getUsername());
        if(model==null) {
            log.error("NO SE ENCONTRO USUARIO, Error durante el login para el usuario: {}", data.getUsername());
            throw new BusinessException(ErrorCode.LOGIN_ERROR);
        }
        if(model.getBloqueado()==1){
            //BLOQUEADO, NO SE PUEDE NI INTENTAR
            log.error("Error durante el login para el usuario: {}, Está Bloqueado", data.getUsername());
            throw new BusinessException(ErrorCode.BLOQUEO_ERROR);
        }
        String salt = model.getSalt();
        String newPassword = data.getPassword()+salt+PEPPER;
        String hashPassword = this.encriptarPassword(newPassword);
        log.info("HashPassport almacenado: hashPassport: {}", model.getPassword());
        log.info("HashPassport ingresado:hashPassport: {}", hashPassword);
        //NO SE PORQUE NO ME LO QUIERE TOMAR EN BOOLEANO PERO BUE
        if(!model.getPassword().equals(hashPassword)) {
            model.aumentarIntentos();
            if(model.getBloqueado()==1) {
                //ESTA BLOQUEADO Y HAY QUE NOTIFICAR POR EMAIL
                //TODO
                usuariosAdapter.guardarUsuario(model);
                log.error("Error durante el login para el usuario: {}", data.getUsername());
                throw new BusinessException(ErrorCode.BLOQUEO_ERROR);
            } else {
                usuariosAdapter.guardarUsuario(model);
                log.error("Error durante el login para el usuario: {}", data.getUsername());
                throw new BusinessException(ErrorCode.LOGIN_ERROR);
            }
        }
        log.info("LOGIN EXITOSO para el usuario: {}", data.getUsername());
        //SI EL USUARIO TENIA PREVIO INTENTOS REALIZADOS, SE RESETEAN A 0
        if(model.getIntentos()>0) {
            model.setIntentos(0);
            usuariosAdapter.guardarUsuario(model);
        }
        return this.crearJWT(model);
    }

    public void restaurarContrasenia(UserRestaurarPasswordRequest data) throws NoSuchAlgorithmException {
        log.info("Restaurar contraseña para el usuario: {}", data.getUsername());
        UsuarioModel model = usuariosAdapter.obtenerUser(data.getUsername());
        String salt = this.createSalt();
        String newPassword = data.getPassword()+salt+PEPPER;
        String hashPassword = this.encriptarPassword(newPassword);
        model.setPassword(hashPassword);
        model.setSalt(salt);
        model.setBloqueado(0);
        model.setIntentos(0);
        usuariosAdapter.guardarUsuario(model);
        log.info("Contraseña nueva guardada");
    }

    private KeyPair crearKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private String crearJWT(UsuarioModel usuarioModel) throws Exception {
        KeyPair keyPair = this.crearKey();
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        String token = JWT.create()
                .withIssuer("auth0")
                .withClaim("USERNAME", usuarioModel.getUsername())
                .withClaim("EMAIL", usuarioModel.getEmail())
                .withExpiresAt(calendar.getTime())
                .withIssuedAt(Instant.now())
                .sign(algorithm);
        return token;


    }

    private String encriptarPassword(String password) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashbytes);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private String createSalt() {
        Generex generex = new Generex("(([a-z]|[A-Z]|[0-9]){3,4})");
        // Generate random String
        String randomStr = generex.random();
        log.info("Salt generado: {}", randomStr);
        return randomStr;
    }

}
