package msAutenticacion.services;

import freemarker.core.ParseException;
import freemarker.template.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Configuration config;

    public Boolean sendConfirmEmail(String toEmail, String subject, Usuario usuario, String codigoActivacion) {
        log.info(">> Servicio de email para FINALIZAR REGISTRO a: {}", toEmail);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            log.info("-- Creando armado de email para: {}", toEmail);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template t = config.getTemplate("email-confirm.ftl");
            Map<String, Object> model = new HashMap<>();
            model.put("Name", usuario.getUsername());
            model.put("cumbion", usuario.getConfirmCodigo());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(toEmail);
            helper.setFrom("webmaster@ecoswap.com.ar");
            helper.setText(html, true);
            helper.setSubject(subject);
            helper.setText(this.emailConfirmEmail(usuario), true);
            log.info("-- Intento de enviar Email a: {}", toEmail);
            javaMailSender.send(mimeMessage);
            log.info("<< Email fue enviado con éxito a: {}", toEmail);
        } catch (Exception e) {
            log.error("<< Error durante el envio de email a: {}, descripcion: {}", toEmail, e);
            throw new RuntimeException(e);
        }
        return true;
    }

    public void enviarEmailConfirmacion(Usuario usuario, String codigoConfirmacion) {
        /*
        Método asincrónico, obtenido de https://www.baeldung.com/java-asynchronous-programming
        Tiene la ventaja de ser método nativo de Java 8.
         */
        CompletableFuture.supplyAsync(() -> {
            try {
                return sendConfirmEmail(usuario.getEmail(), "Gracias por sumarte a ECOSWAP", usuario, codigoConfirmacion);
            } catch (Exception e) {
                log.error("Error al enviar correo de confirmación: {}", e.getMessage(), e);
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
            try {
                return sendConfirmEmail(usuario.getEmail(), "Reenvío de código de confirmación", usuario, codigoConfirmacion);
            } catch (Exception e) {
                log.error("Error al enviar correo de confirmación: {}", e.getMessage(), e);
                return false; // O manejar el error de acuerdo a tus necesidades
            }
        });
    }

    private String emailConfirmEmail(Usuario usuario) {
        String userId = Long.toString(usuario.getIdUsuario());
        String linkBase = "";
        String htmlConfirmEmail = "<!DOCTYPE html> \n" +
                "                <html lang=\"en\"> \n" +
                "                <head> \n" +
                "                 \n" +
                "                </head> \n" +
                "                 \n" +
                "                <body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\"> \n" +
                "                <center style=\"width: 100%; background-color: #f1f1f1;\"> \n" +
                "                    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\"> \n" +
                "                      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp; \n" +
                "                    </div> \n" +
                "                    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\"> \n" +
                "                    <!-- BEGIN BODY --> \n" +
                "                      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\"> \n" +
                "                      <tr> \n" +
                "                          <td valign=\"op\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\"> \n" +
                "                          <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"> \n" +
                "                          <tr> \n" +
                "                          <td class=\"logo\" style=\"ext-aling: center;\"> \n" +
                "                            <h1><a href=\"https://ecoswap.com.ar\">Ecoswap</a></h1> \n" +
                "                          </td> \n" +
                "                          </tr> \n" +
                "                          </table> \n" +
                "                          </td> \n" +
                "                      </tr><!-- end tr --> \n" +
                "                      <tr> \n" +
                "                          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 3em 0 2em 0; text-align: center;\"> \n" +
                "                            <img src=\"https://cdn.discordapp.com/attachments/738465420318736417/1154943032257421322/logo_green_small.png\" alt=\"\" style=\"width: 300px; max-width: 600px; height: auto; margin: auto; display: block;\"> \n" +
                "                          </td> \n" +
                "                      </tr><!-- end tr --> \n" +
                "                <tr> \n" +
                "                          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\"> \n" +
                "                            <table> \n" +
                "                            <tr> \n" +
                "                            <td> \n" +
                "                            <div class=\"ext\" style=\"padding: 0 2.5em; text-align: center;\"> \n" +
                "                            <h2>Por favor, confirme su email</h2> \n" +
                "                            <h3>La economía circular dando nueva vida</h3> \n" +
                "                            <p><a href=\"https://www.youtube.com/watch?v=gEiqSUzQ1fo\" class=\"btn btn-primary\">Confirmar email</a></p> \n" +
                "                                            <h4>O sino ingresando desde el siguiente link:</h4> \n" +
                "                                            <h4><a href=\"https://www.youtube.com/watch?v=gEiqSUzQ1fo\"> www.ecoswap.com.ar/confirmarEmail</a></h4> \n" +
                "                                             \n" +
                "                            </div> \n" +
                "                            </td> \n" +
                "                            </tr> \n" +
                "                            </table> \n" +
                "                          </td> \n" +
                "                      </tr><!-- end tr --> \n" +
                "                      <!-- 1 Column Text  Button : END --> \n" +
                "                      </table> \n" +
                "                      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\"> \n" +
                "                      <tr> \n" +
                "                          <td valign=\"middle\" class=\"bg_light footer email-section\"> \n" +
                "                            <table> \n" +
                "                            <tr> \n" +
                "                                <td valign=\"op\" width=\"33.333%\" style=\"padding-top: 20px;\"> \n" +
                "                                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\"> \n" +
                "                                    <tr> \n" +
                "                                      <td style=\"ext-align: left; padding-right: 10px;\"> \n" +
                "                                      <h3 class=\"heading\">Acerca de nosotros</h3> \n" +
                "                                      <p>Somos una plataforma de la economía circular donde podrás encontrar una nueva vida a esos objetos que ya no pensas utilizar.</p> \n" +
                "                                      </td> \n" +
                "                                    </tr> \n" +
                "                                  </table> \n" +
                "                                </td> \n" +
                "                                <td align=\"op\" width=\"33.333%\" style=\"padding-top: 20px;\"> \n" +
                "                                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\"> \n" +
                "                                    <tr > \n" +
                "                                      <td style=\"ext-align: left; padding-left: 5px; padding-right: 5px;\"> \n" +
                "                                      <h3 class=\"heading\">Contacto</h3> \n" +
                "                                      <ul> \n" +
                "                                <span class=\"ext\">consultas@ecoswap.com</span> \n" +
                "                              </ul> \n" +
                "                                      </td> \n" +
                "                                    </tr> \n" +
                "                                  </table> \n" +
                "                                </td> \n" +
                "                                \n" +
                "                              </tr> \n" +
                "                            </table> \n" +
                "                          </td> \n" +
                "                        </tr><!-- end: tr --> \n" +
                "                      </table> \n" +
                "                 \n" +
                "                    </div> \n" +
                "                  </center> \n" +
                "                </body> \n" +
                "                </html>";
        log.info("-- Finaliza el armado de email para CONFIRMAR EMAIL de {}", usuario.getEmail());
        return htmlConfirmEmail;
    }

    private String emailChangePassword() {
        return "";
    }

    private String emailAccountDissable() {
        return "";
    }

    private String emailAccountBlocked() {
        return "";
    }

    private String headerBasicTemplate() {
        return "";
    }

}
