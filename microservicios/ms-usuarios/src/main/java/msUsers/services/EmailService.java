package msUsers.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarEmailConTicketPaquete(Usuario usuario, String pdfBase64) {
        /*
        Método asincrónico, obtenido de https://www.baeldung.com/java-asynchronous-programming
        Tiene la ventaja de ser método nativo de Java 8.
         */
        CompletableFuture.supplyAsync(() -> {
            try {
                return sendEmailWithAttrachment(usuario.getEmail(), "Datos de envio de paquete", usuario, pdfBase64);
            } catch (Exception e) {
                log.error("Error al enviar correo de orden: {}", e.getMessage(), e);
                return false; // O manejar el error de acuerdo a tus necesidades
            }
        });
    }

    private Boolean sendEmailWithAttrachment(String toEmail, String subject, Usuario usuario, String pdfBase64) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("no-responde@ecoswap.com");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText("Hola " + usuario.getUsername() + ". Imprima y pegue el ticket en su paquete para enviar " +
                "en el coreo mas cercano de Correo Argentino.");
        mimeMessageHelper.setSubject(subject);

        // Now, using ByteArrayInputStream to
        // get the bytes of the string, and
        // converting them to InputStream
        InputStream stream = new ByteArrayInputStream(pdfBase64.getBytes
                (StandardCharsets.UTF_8));

        mimeMessageHelper.addAttachment("Orden_de_envio.pdf", new InputStreamResource(stream));

        javaMailSender.send(mimeMessage);
        log.info("Email fue enviado con éxito a: {}", toEmail);
        return true;
    }

}
