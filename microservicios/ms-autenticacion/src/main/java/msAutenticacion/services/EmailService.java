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
            model.put("cumbion", "gEiqSUzQ1fo");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(toEmail);
            helper.setText(html, true);
            helper.setSubject(subject);
         //   helper.setText(this.emailConfirmEmail(usuario), true);
            log.info("-- Intento de enviar Email a: {}", toEmail);
            javaMailSender.send(mimeMessage);
            log.info("<< Email fue enviado con éxito a: {}", toEmail);
        } catch (MessagingException e) {
            log.error("<< Error durante el envio de email a: {}", toEmail);
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (TemplateNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (MalformedTemplateNameException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private String emailConfirmEmail(Usuario usuario) {
        String userId = Long.toString(usuario.getId());
        String linkBase = "";
        String htmlConfirmEmail = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body width=\"100%\" style=\"margin: 0; padding: 0 !important; mso-line-height-rule: exactly; background-color: #f1f1f1;\">\n" +
                "\t<center style=\"width: 100%; background-color: #f1f1f1;\">\n" +
                "    <div style=\"display: none; font-size: 1px;max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                "      &zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;&zwnj;&nbsp;\n" +
                "    </div>\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto;\" class=\"email-container\">\n" +
                "    \t<!-- BEGIN BODY -->\n" +
                "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                "      \t<tr>\n" +
                "          <td valign=\"top\" class=\"bg_white\" style=\"padding: 1em 2.5em 0 2.5em;\">\n" +
                "          \t<table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "          \t\t<tr>\n" +
                "          \t\t\t<td class=\"logo\" style=\"text-align: center;\">\n" +
                "\t\t\t            <h1><a href=\"http://ecoswap-web.s3-website-sa-east-1.amazonaws.com/\">Ecoswap</a></h1>\n" +
                "\t\t\t          </td>\n" +
                "          \t\t</tr>\n" +
                "          \t</table>\n" +
                "          </td>\n" +
                "\t      </tr><!-- end tr -->\n" +
                "\t      <tr>\n" +
                "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 3em 0 2em 0; text-align: center;\">\n" +
                "            <img src=\"images/email.png\" alt=\"\" style=\"width: 300px; max-width: 600px; height: auto; margin: auto; display: block;\">\n" +
                "          </td>\n" +
                "\t      </tr><!-- end tr -->\n" +
                "\t\t\t\t<tr>\n" +
                "          <td valign=\"middle\" class=\"hero bg_white\" style=\"padding: 2em 0 4em 0;\">\n" +
                "            <table>\n" +
                "            \t<tr>\n" +
                "            \t\t<td>\n" +
                "            \t\t\t<div class=\"text\" style=\"padding: 0 2.5em; text-align: center;\">\n" +
                "            \t\t\t\t<h2>Por favor, confirme su email</h2>\n" +
                "            \t\t\t\t<h3>La economía circular dando nueva vida</h3>\n" +
                "            \t\t\t\t<p><a href=\"https://www.youtube.com/watch?v=gEiqSUzQ1fo\" class=\"btn btn-primary\">Confirmar email</a></p>\n" +
                "                            <h4>O sino ingresando desde el siguiente link:</h4>\n" +
                "                            <h4><a href=\"https://www.youtube.com/watch?v=gEiqSUzQ1fo\"> www.ecoswap.com/confirmarEmail</a></h4>\n" +
                "                            \n" +
                "            \t\t\t</div>\n" +
                "            \t\t</td>\n" +
                "            \t</tr>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "\t      </tr><!-- end tr -->\n" +
                "      <!-- 1 Column Text + Button : END -->\n" +
                "      </table>\n" +
                "      <table align=\"center\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: auto;\">\n" +
                "      \t<tr>\n" +
                "          <td valign=\"middle\" class=\"bg_light footer email-section\">\n" +
                "            <table>\n" +
                "            \t<tr>\n" +
                "                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">\n" +
                "                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                "                    <tr>\n" +
                "                      <td style=\"text-align: left; padding-right: 10px;\">\n" +
                "                      \t<h3 class=\"heading\">Acerca de nosotros</h3>\n" +
                "                      \t<p>Somos una plataforma de la economía circular donde podrás encontrar una nueva vida a esos objetos que ya no pensas utilizar.</p>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "                <td valign=\"top\" width=\"33.333%\" style=\"padding-top: 20px;\">\n" +
                "                  <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\n" +
                "                    <tr >\n" +
                "                      <td style=\"text-align: left; padding-left: 5px; padding-right: 5px;\">\n" +
                "                      \t<h3 class=\"heading\">Contacto</h3>\n" +
                "                      \t<ul>\n" +
                "\t\t\t\t\t                <span class=\"text\">consultas@ecoswap.com</span>\n" +
                "\t\t\t\t\t              </ul>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "               \n" +
                "              </tr>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "        </tr><!-- end: tr -->\n" +
                "      </table>\n" +
                "\n" +
                "    </div>\n" +
                "  </center>\n" +
                "</body>\n" +
                "</html>";
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
