package msAutenticacion.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Direccion;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.DireccionRepository;
import msAutenticacion.domain.repositories.UsuarioRepository;
import msAutenticacion.domain.requests.RequestLogin;
import msAutenticacion.domain.requests.RequestPassword;
import msAutenticacion.domain.requests.RequestSignin;
import msAutenticacion.domain.requests.propuestas.RequestDireccion;
import msAutenticacion.exceptions.LoginUserException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FundacionService fundacionService;
    private final ParticularService particularService;
    private  final DireccionRepository direccionRepository;
    private final EmailService emailService;
    private static final String JSON = "application/JSON";

    private static final String PEPPER = "c";

    public UsuarioService(UsuarioRepository usuarioRepository,
                          FundacionService fundacionService,
                          ParticularService particularService,
                          DireccionRepository direccionRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.fundacionService = fundacionService;
        this.particularService = particularService;
        this.direccionRepository = direccionRepository;
        this.emailService = emailService;
    }

    public Optional<Usuario> obtenerUsuario(Long userId) {
        return usuarioRepository.findById(userId);
    }
    public Long crearUsuario(RequestSignin signin) {
        log.info("crearUsuario: Usuario a crear:" + signin.getUsername());
        RequestDireccion direccionCrear = signin.getDireccion();
        String salt = this.crearSalt();
        Usuario usuario = this.crearUsuario(signin, salt);
        Direccion direccion = this.crearDireccion(usuario, direccionCrear);
        Direccion direccionCreada = direccionRepository.save(direccion);
        log.info("crearUsuario: Direccion creado con ID:" + direccionCreada.getIdDireccion());
        Usuario usuarioCreado = null;
        if(usuario.isSwapper()) {
            usuarioCreado = particularService.crearUser(direccionCreada.getUsuario(), signin);
        } else {
            usuarioCreado = fundacionService.crearUser(direccionCreada.getUsuario(), signin);
        }
        log.info("crearUsuario: Usuario creado con ID:" + usuarioCreado.getIdUsuario());
        emailService.sendConfirmEmail(usuario.getEmail(), "Bienvenido a ECOSWAP", usuario, this.crearSalt());
        log.info("Email enviado para el usuario:" + usuario.getUsername());
        return usuarioCreado.getIdUsuario();
    }
    


    public void actualizarContrasenia(RequestPassword request) {
        log.info(("actualizarContrasenia: Actualizar contraseña para usuarioId: " + request.getUsername()));
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + request.getUsername()));
        usuario.setPassword(request.getNuevoPassword());
        usuarioRepository.save(usuario);
        log.info(("actualizarContrasenia: Se ha actualizar con ÉXITO la contraseña para usuarioId: " + request.getUsername()));
    }

    public boolean login(RequestLogin request) {
        log.info(("login: Intentar ingresar el username: " + request.getUsername()));
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + request.getUsername()));
        String hashPassword = this.crearPassword(request.getPassword(), usuario.getSalt());
        if(!this.compararContrasenias(hashPassword, usuario.getPassword())) {
            //ERROR, LA PASSWORD NO FUNCA
            log.error(("login: error durante el LOGIN y se le aumenta los intentos en 1: " + request.getUsername()));
            usuario.aumentarIntetoEn1();
            if(usuario.getIntentos()>2) {
                log.error(("login: Usuario será bloqueado por superar la cantidad de intentos fallidos: " + request.getUsername()));
                usuario.setBloqueado(true);
                usuarioRepository.save(usuario);
                throw new LoginUserException("El usuario fue bloqueado");
            }
            usuarioRepository.save(usuario);
            return false;
        }
        log.info(("login: Login EXITOSO para username: " + request.getUsername()));
        return true;
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

    private Usuario crearUsuario(RequestSignin signin, String salt) {
        return Usuario.builder()
                .email(signin.getEmail())
                .username(signin.getUsername())
                .password(this.crearPassword(signin.getPassword(), salt))
                .salt(salt)
                .telefono(signin.getTelefono())
                .isSwapper(signin.getFundacion()==null)
                .intentos(0)
                .bloqueado(false)
                .build();
    }
    
    /*
    private String crearJWT(Usuario usuario) {
        String prvKey = "-----BEGIN PRIVATE KEY-----\n"
                + "........\n"
                + "-----END PRIVATE KEY-----";
        prvKey = prvKey.replace("-----BEGIN PRIVATE KEY-----", "");
        prvKey = prvKey.replace("-----END PRIVATE KEY-----", "");
        prvKey = prvKey.replaceAll("\\s+","");

        byte [] prvKeyBytes = Base64.decode(prvKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(prvKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey prvKey = kf.generatePrivate(keySpec);
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            return JWT.create()
                    .withIssuer("ecoswap")
                    .withExpiresAt(Instant.now().plusSeconds(604800))
                    .withClaim("email", usuario.getEmail())
                    .withClaim("id", usuario.getIdUsuario())
                    .withClaim("esParticular", usuario.isSwapper())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            return null;
        }
    }
    */

}
