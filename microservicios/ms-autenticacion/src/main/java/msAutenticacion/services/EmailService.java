package msAutenticacion.services;

import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public Boolean sendConfirmEmail(String toEmail, String subject, Usuario usuario, String codigoActivacion) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-responde@ecoswap.com");
        message.setTo(toEmail);
        message.setText("Hola " + usuario.getUsername() + ", bienvenido seas. A continuación ingrese el " +
                "siguiente código para poder finalizar su registro: " +codigoActivacion);
        message.setSubject(subject);
        javaMailSender.send(message);
        log.info("Mail fue enviado con éxito a: {}", toEmail);
        return true;
    }

    private Boolean sendMail(String toEmail, String subject, Usuario usuario, String codigoActivacion) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-responde@ecoswap.com");
        message.setTo(toEmail);
        message.setText("Hola " + usuario.getUsername() + ", bienvenido seas. A continuación ingrese el " +
                "siguiente código para poder finalizar su registro: " +codigoActivacion);
        message.setSubject(subject);
        javaMailSender.send(message);
        log.info("Mail fue enviado con éxito a: {}", toEmail);
        return true;
    }
}
