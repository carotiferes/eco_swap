package msAutenticacion.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Direccion;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.DireccionRepository;
import msAutenticacion.domain.repositories.UsuarioRepository;
import msAutenticacion.domain.requests.RequestConfirm;
import msAutenticacion.domain.requests.RequestLogin;
import msAutenticacion.domain.requests.RequestPassword;
import msAutenticacion.domain.requests.RequestSignUp;
import msAutenticacion.domain.requests.propuestas.RequestDireccion;
import msAutenticacion.exceptions.LoginUserBlockedException;
import msAutenticacion.exceptions.LoginUserWrongCredentialsException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FundacionService fundacionService;
    private final ParticularService particularService;
    private  final DireccionRepository direccionRepository;
    private final EmailService emailService;
    private static final String JSON = "application/JSON";
    private static final String prvKey = "3c0s2ap231023914523";

    private static final String PEPPER = "c";

    public UsuarioService(UsuarioRepository usuarioRepository,
                          FundacionService fundacionService,
                          ParticularService particularService,
                          DireccionRepository direccionRepository,
                          EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.fundacionService = fundacionService;
        this.particularService = particularService;
        this.direccionRepository = direccionRepository;
        this.emailService = emailService;
    }

    public Optional<Usuario> obtenerUsuario(Long userId) {
        return usuarioRepository.findById(userId);
    }
    public Long crearUsuario(RequestSignUp signUp) {
        log.info("crearUsuario: Usuario a crear:" + signUp.getUsername());
        RequestDireccion direccionCrear = signUp.getDireccion();
        Usuario usuario = this.crearUsuarioBuilder(signUp);
        Direccion direccion = this.crearDireccion(usuario, direccionCrear);
        Direccion direccionCreada = direccionRepository.save(direccion);
        log.info("crearUsuario: Direccion creado con ID:" + direccionCreada.getIdDireccion());
        Usuario usuarioCreado = null;
        if(usuario.isSwapper()) {
            usuarioCreado = particularService.crearUser(direccionCreada.getUsuario(), signUp);
        } else {
            usuarioCreado = fundacionService.crearUser(direccionCreada.getUsuario(), signUp);
        }
        log.info("crearUsuario: Usuario creado con ID: {}", usuarioCreado.getIdUsuario());
        this.enviarEmailConfirmacion(usuarioCreado, usuarioCreado.getConfirmCodigo());
        return usuarioCreado.getIdUsuario();
    }

    private void enviarEmailConfirmacion(Usuario usuario, String codigoConfirmacion) {
        /*
        Método asincrónico, obtenido de https://www.baeldung.com/java-asynchronous-programming
        Tiene la ventaja de ser método nativo de Java 8.
         */
        CompletableFuture.supplyAsync(() ->
                emailService.sendConfirmEmail(
                        usuario.getEmail(),
                        "Gracias por sumarte a ECOSWAP",
                        usuario,
                        codigoConfirmacion));
    }
    


    public void actualizarContrasenia(RequestPassword request) {
        log.info(("actualizarContrasenia: Actualizar contraseña para usuarioId: " + request.getUsername()));
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + request.getUsername()));
        usuario.setPassword(request.getNuevoPassword());
        usuarioRepository.save(usuario);
        log.info(("actualizarContrasenia: Se ha actualizar con ÉXITO la contraseña para usuarioId: " + request.getUsername()));
    }

    public Boolean confirmarUsuario(RequestConfirm request) {
        log.info("confirmarUsuario: Confirmar usuarioId {}", request.getUsername());
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuarioId: " + request.getUsername()));
        if(usuario.getConfirmCodigo().equals(request.getCodigo())){
            usuario.setConfirmCodigo("");
            usuario.setBloqueado(false);
            usuarioRepository.save(usuario);
            log.info(("confirmarUsuario: Confirmación exitosa para userId: "
                    + request.getUsername()));
            return true;
        }
        log.info("confirmarUsuario: Confirmación de ususario para usuarioId {} falló: ", request.getUsername());
        throw new EntityNotFoundException("Error durante la confirmación del usaurioId: " + request.getUsername());
    }

    public String login(RequestLogin request) throws NoSuchAlgorithmException {
        log.info(("login: Intentar ingresar el username: " + request.getUsername()));
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + request.getUsername()));

        String hashPassword = this.crearPassword(request.getPassword(), usuario.getSalt());
        if(!usuario.isBloqueado()){
            if(!this.compararContrasenias(hashPassword, usuario.getPassword())) {
                //ERROR, LA PASSWORD NO FUNCA
                log.error(("login: error durante el LOGIN y se le aumenta los intentos en 1: " + request.getUsername()));
                usuario.aumentarIntentoEn1();
                if(usuario.getIntentos()>2) {
                    log.error(("login: Usuario será bloqueado por superar la cantidad de intentos fallidos: " + request.getUsername()));
                    usuario.setBloqueado(true);
                    usuarioRepository.save(usuario);
                    throw new LoginUserBlockedException("El usuario fue bloqueado");
                }
                log.error("Cantidad de intentos actual: {}", usuario.getIntentos());
                usuarioRepository.save(usuario);
                throw new LoginUserWrongCredentialsException("Usuario y/o contraseña invalido");
            }
        }
        else
            throw new LoginUserBlockedException("El usuario fue bloqueado");
        log.info(("login: Login EXITOSO para username: " + request.getUsername()));
        usuario.setIntentos(0);
        usuarioRepository.save(usuario);
        return this.crearJWT(usuario);
    }

    private Boolean compararContrasenias(String passwordHashIngresado, String passwordHashGuardado) {
        return passwordHashGuardado.equals(passwordHashIngresado);
    }

    private String crearPassword(String password, String salt) {
        String passwordAHashear = password + salt + PEPPER;
        return getSHA256(passwordAHashear);
    }

    private static String getSHA256(String input){

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    private String crearSalt() {
        return RandomStringUtils.randomAlphanumeric(5);
    }

    private Direccion crearDireccion(Usuario usuario, RequestDireccion direccionCrear) {
        return Direccion.builder()
                .usuario(usuario)
                .direccion(direccionCrear.getDireccion())
                .codigoPostal(direccionCrear.getCodigoPostal())
                .altura(direccionCrear.getAltura())
                .dpto(direccionCrear.getDepartamento())
                .piso(direccionCrear.getPiso())
                .build();
    }

    private Usuario crearUsuarioBuilder(RequestSignUp signin) {
        String salt = this.crearSalt();
        return Usuario.builder()
                .email(signin.getEmail())
                .username(signin.getUsername())
                .password(this.crearPassword(signin.getPassword(), salt))
                .salt(salt)
                .confirmCodigo(this.crearSalt())
                .telefono(signin.getTelefono())
                .isSwapper(signin.getFundacion()==null)
                .intentos(0)
                .puntaje(0)
                .bloqueado(false) //CUANDO SE CREA UN USUARIO, ESTE DEBE CONFIRMAR POR MAIL PRIMERO.
                .build();

        // El bloqueado en false, es temporal, hasta que se ponga para ingresar el codigo de confirmación del email.
    }
    

    private String crearJWT(Usuario usuario) throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.generateKeyPair();
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) kp.getPublic(), (RSAPrivateKey) kp.getPrivate());
            return JWT.create()
                    .withIssuer("ecoswap")
                    .withExpiresAt(Instant.now().plusSeconds(604800))
                    .withClaim("email", usuario.getEmail())
                    .withClaim("id", usuario.getIdUsuario())
                    .withClaim("esParticular", usuario.isSwapper())
                    .sign(algorithm);
        } catch (JWTCreationException | NoSuchAlgorithmException exception){
            log.error(("login: JWT dió error durante la creación: " + exception.getMessage()));
            return "";
        }
    }

}
