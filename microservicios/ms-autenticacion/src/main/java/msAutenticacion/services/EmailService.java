package msAutenticacion.services;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void enviarEmailConfirmacion(Usuario usuario, String codigoConfirmacion) {
        /*
        Método asincrónico, obtenido de https://www.baeldung.com/java-asynchronous-programming
        Tiene la ventaja de ser método nativo de Java 8.
         */
        CompletableFuture.supplyAsync(() -> {
         //   log.info("INTENTAMOS ENVIAR UN MAIL a: {}", usuario.getEmail());
            try {
                return sendConfirmEmail(usuario.getEmail(), "Gracias por sumarte a ECOSWAP", usuario, codigoConfirmacion);
            } catch (Exception e) {
                log.info("Error al enviar correo de confirmación: {}", e.getMessage(), e);
                return false; // O manejar el error de acuerdo a tus necesidades
            }
        });
    }

    public void reenvioEmailConfirmacion(Usuario usuario, String codigoConfirmacion) {
        /*
        Método asincrónico, obtenido de https://www.baeldung.com/java-asynchronous-programming
        Tiene la ventaja de ser método nativo de Java 8.
         */
        CompletableFuture.supplyAsync(() -> {
          //  log.info("INTENTAMOS ENVIAR UN MAIL a: {}", usuario.getEmail());
            try {
                return sendConfirmEmail(usuario.getEmail(), "Reenvío de código de confirmación", usuario, codigoConfirmacion);
            } catch (Exception e) {
                log.error("Error al enviar correo de confirmación: {}", e.getMessage(), e);
                return false; // O manejar el error de acuerdo a tus necesidades
            }
        });
    }

    private Boolean sendConfirmEmail(String toEmail, String subject, Usuario usuario, String codigoActivacion) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("webmaster@ecoswap.com.ar");
        message.setTo(toEmail);
        message.setText("¡Hola " + usuario.getUsername() + "!\nTe damos la bienvenida a EcoSwap. \n"+
                "Con tu cuenta vas a poder donar a fundaciones que lo necesiten, y crear publicaciones para intercambiar y vender!" +
                "\n\nPara terminar tu registro vas a necesitar el siguiente código: " +codigoActivacion);
        message.setSubject(subject);
        javaMailSender.send(message);
        log.info("Email fue enviado con éxito a: {}", toEmail);
        return true;
    }

}
