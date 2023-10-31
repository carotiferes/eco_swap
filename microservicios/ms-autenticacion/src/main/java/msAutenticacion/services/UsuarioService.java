package msAutenticacion.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Direccion;
import msAutenticacion.domain.entities.Fundacion;
import msAutenticacion.domain.entities.Particular;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.entities.enums.TipoDocumento;
import msAutenticacion.domain.repositories.DireccionRepository;
import msAutenticacion.domain.repositories.UsuarioRepository;
import msAutenticacion.domain.requests.*;
import msAutenticacion.domain.requests.propuestas.RequestDireccion;
import msAutenticacion.domain.responses.ResponseUpdateEntity;
import msAutenticacion.exceptions.*;
import msAutenticacion.exceptions.events.UsuarioCreadoEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private FundacionService fundacionService;
    @Autowired
    private ParticularService particularService;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private  CriteriaBuilderQueries criteriaBuilderQueries;
    private static final String JSON = "application/JSON";
    private static final String prvKey = "3c0s2ap231023914523";
    private static final String PEPPER = "c";

    public Optional<Usuario> obtenerUsuario(Long userId) {
        return usuarioRepository.findById(userId);
    }

    public Usuario crearUsuario(RequestSignUp signUp){

        if(this.usuarioRepository.findByUsername(signUp.getUsername()).isPresent())
            throw new UserDuplicatedException("Ya hay alguien registrado con el email " + signUp.getEmail() + " en nuestra base de datos.");

        log.info("crearUsuario: Usuario a crear:" + signUp.getUsername());
        RequestDireccion direccionCrear = signUp.getDireccion();
        Usuario usuario = this.crearUsuarioBuilder(signUp);
        Direccion direccion = this.crearDireccion(usuario, direccionCrear);
        Direccion direccionCreada = direccionRepository.save(direccion);
        log.info("crearUsuario: Direccion creado con ID:" + direccionCreada.getIdDireccion());
        Usuario usuarioCreado;

        try {
            if (usuario.isSwapper())
                usuarioCreado = particularService.crearUser(usuario, signUp);
            else
                usuarioCreado = fundacionService.crearUser(usuario, signUp);

            log.info("crearUsuario: Usuario creado con ID: {}", usuarioCreado.getIdUsuario());
            UsuarioCreadoEvent usuarioCreadoEvent = new UsuarioCreadoEvent(this, usuarioCreado);
            eventPublisher.publishEvent(usuarioCreadoEvent);
            return usuarioCreado;
        } catch (Exception e) {
            throw new UserCreationException("Error en la creación del usuario: " + e.getMessage());
        }
    }

    public ResponseUpdateEntity editarUsuario(RequestEditProfile requestEditProfile, Usuario user){
        try{
            eliminarDireccionesAntiguas(user);
            crearNuevaDireccion(requestEditProfile, user);
            actualizarUsuario(requestEditProfile, user);
            entityManager.merge(user);

            ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
            responseUpdateEntity.setStatus(HttpStatus.OK.name());
            responseUpdateEntity.setDescripcion("Perfil editado correctamente");
            log.info("<< Perfil {} editado correctamente", user.getEmail());
            return responseUpdateEntity;
        }
        catch(Exception e){
            ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
            responseUpdateEntity.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseUpdateEntity.setDescripcion("Error al intentar editar el perfil " + user.getEmail() + "El mensaje de error es " + e.getMessage());
            log.info("<< Error al intentar editar el perfil {}. El mensaje de error es: {} ", user.getEmail(), e.getMessage());
            return responseUpdateEntity;
        }
    }

    public void actualizarContrasenia(RequestPassword request, Long idUsuario) {
        log.info(("actualizarContrasenia: Actualizar contraseña para usuarioId: " + idUsuario));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + idUsuario));

        String newPassword = crearPassword(request.getNuevaPassword(),usuario.getSalt());
        if(!Objects.equals(newPassword, usuario.getPassword()))
            usuario.setPassword(crearPassword(request.getNuevaPassword(),usuario.getSalt()));
        else
            throw new PasswordUpdateException("No es válido cambiar por la misma contraseña. Por favor, ingresar otra.");

        usuario.setPassword(newPassword);
        usuarioRepository.save(usuario);
        log.info(("actualizarContrasenia: Se ha actualizar con ÉXITO la contraseña para usuarioId: " + usuario.getUsername()));
    }

    public ResponseUpdateEntity editarAvatar(RequestEditAvatar requestEditAvatar, Usuario usuario){

        usuario.setAvatar(requestEditAvatar.getAvatar());
        entityManager.merge(usuario);
        log.info("<< Avatar editado correctamente.");

        ResponseUpdateEntity responseUpdateEntity = new ResponseUpdateEntity();
        responseUpdateEntity.setStatus(HttpStatus.OK.name());
        responseUpdateEntity.setDescripcion("Avatar editado correctamente.");

        return responseUpdateEntity;
    }

    public Boolean confirmarUsuario(RequestConfirm request) {
        log.info("confirmarUsuario: Confirmar usuarioId {}", request.getIdUsuario());
        try {
            Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario con ID: " + request.getIdUsuario()));

            if(usuario.isValidado())
                throw new ValidationUserException("Usuario ya validado.");

            if (usuario.getConfirmCodigo().equals(request.getCodigo())) {
                usuario.setConfirmCodigo("");
                usuario.setBloqueado(false);
                usuario.setValidado(true);
                usuarioRepository.save(usuario);
                log.info(("confirmarUsuario: Confirmación exitosa para userId: " + usuario.getUsername()));
                return true;
            } else {
                log.info("confirmarUsuario: Código inválido.");
                throw new ValidationUserException("Error durante la confirmación del usuario: " + usuario.getUsername() + ". Código inválido.");
            }
        } catch (EntityNotFoundException e) {
            log.error("Error durante la confirmación del usuario: " + e.getMessage());
            return false;
        }
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
                throw new LoginUserWrongCredentialsException("Usuario y/o contraseña inválido");
            }
        }
        else
            throw new LoginUserBlockedException("El usuario fue bloqueado");
        log.info(("login: Login EXITOSO para username: " + request.getUsername()));
        usuario.setIntentos(0);

        String secretJWT = this.crearSalt();
        usuario.setSecretJWT(secretJWT);
        usuarioRepository.save(usuario);
        return this.crearJWT(usuario, secretJWT);
    }

    private Boolean compararContrasenias(String passwordHashIngresado, String passwordHashGuardado) {
        return passwordHashGuardado.equals(passwordHashIngresado);
    }

    public void reenviarCodigoConfirmacion(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("No fue encontrado el usuario: " + idUsuario));

        String nuevoTkn = this.crearSalt();
        usuario.setConfirmCodigo(nuevoTkn);
        usuarioRepository.save(usuario);
        emailService.reenvioEmailConfirmacion(usuario, nuevoTkn);
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
                .calle(direccionCrear.getCalle())
                .localidad(direccionCrear.getLocalidad())
                .altura(direccionCrear.getAltura())
                .dpto(direccionCrear.getDepartamento())
                .piso(direccionCrear.getPiso())
                .codigoPostal(direccionCrear.getCodigoPostal())
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
                .validado(false)
                .bloqueado(false)
                .build();
    }
    

    private String crearJWT(Usuario usuario, String secretJWT) throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();
           // Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) kp.getPublic(), null); // (RSAPrivateKey) kp.getPrivate());
            Algorithm algorithm = Algorithm.HMAC256(secretJWT);
          //  log.info("DATA JWT: public {}; private {}", kp.getPublic(), kp.getPrivate());
            //return JWT.create()
            String email = usuario.getEmail();
            Long userId = usuario.getIdUsuario();
            Boolean esParticular = usuario.isSwapper();
            Boolean userValidado = usuario.isValidado();
            String jwtCreated = JWT.create()
                    .withIssuer(email)
                    .withExpiresAt(Instant.now().plusSeconds(86400))
                    .withClaim("email", email)
                    .withClaim("id", userId)
                    .withClaim("esParticular", esParticular)
                    .withClaim("usuarioValidado", userValidado)
                    .sign(algorithm);
            return jwtCreated;
        } catch (JWTCreationException | NoSuchAlgorithmException exception){
            log.error(("login: JWT dió error durante la creación: " + exception.getMessage()));
            throw exception;
        }
    }
    private void eliminarDireccionesAntiguas(Usuario user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Direccion> delete = criteriaBuilder.createCriteriaDelete(Direccion.class);
        Root<Direccion> direccionRoot = delete.from(Direccion.class);
        delete.where(criteriaBuilder.equal(direccionRoot.get("usuario"), user));
        entityManager.createQuery(delete).executeUpdate();
    }

    private void crearNuevaDireccion(RequestEditProfile requestEditProfile, Usuario user) {
        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setUsuario(user);
        nuevaDireccion.setCalle(requestEditProfile.getDireccion().getCalle());
        nuevaDireccion.setPiso(requestEditProfile.getDireccion().getPiso());
        nuevaDireccion.setDpto(requestEditProfile.getDireccion().getDepartamento());
        nuevaDireccion.setAltura(requestEditProfile.getDireccion().getAltura());
        nuevaDireccion.setLocalidad(requestEditProfile.getDireccion().getLocalidad());

        List<Direccion> direcciones = new ArrayList<>();
        direcciones.add(nuevaDireccion);
        user.setDirecciones(direcciones);
    }

    private void actualizarUsuario(RequestEditProfile requestEditProfile, Usuario user) {
        user.setTelefono(requestEditProfile.getTelefono());

        if (user.isSwapper()) {
            Optional<Particular> optionalParticular = criteriaBuilderQueries.getParticularPorUsuario(user.getIdUsuario());
            Particular particular = optionalParticular.get();
            particular.setFechaNacimiento(requestEditProfile.getParticular().getFechaNacimiento());
            particular.setDni(requestEditProfile.getParticular().getDni());
            particular.setNombre(requestEditProfile.getParticular().getNombre());
            particular.setApellido(requestEditProfile.getParticular().getApellido());
            particular.setTipoDocumento(TipoDocumento.valueOf(requestEditProfile.getParticular().getTipoDocumento()));
            particular.setPublicKey(requestEditProfile.getParticular().getPublicKey());
            particular.setAccessToken(requestEditProfile.getParticular().getAccessToken());
        } else {
            Optional<Fundacion> optionalFundacion = criteriaBuilderQueries.getFundacionPorUsuario(user.getIdUsuario());
            Fundacion fundacion = optionalFundacion.get();
            fundacion.setCuil(requestEditProfile.getFundacion().getCuil());
            fundacion.setNombre(requestEditProfile.getFundacion().getNombre());
        }
    }

}
