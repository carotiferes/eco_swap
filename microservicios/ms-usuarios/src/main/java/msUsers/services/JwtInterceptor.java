package msUsers.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import msUsers.domain.entities.Usuario;
import msUsers.domain.model.UsuarioContext;
import msUsers.exceptions.responses.UnauthorizedAccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!Objects.equals(request.getMethod(), "OPTIONS")) {
            String jwtToken = request.getHeader("Authorization");

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                log.info("ERROR 401: Acceso no autorizado.");
                UnauthorizedAccessResponse unauthorizedAccessResponse = new UnauthorizedAccessResponse(
                        "ERROR 401: Acceso no autorizado.",
                        System.currentTimeMillis(),
                        HttpStatus.UNAUTHORIZED);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(objectMapper.writeValueAsString(unauthorizedAccessResponse));
                return false;
            }

            String token = jwtToken.substring(7);

            try {
                Usuario usuario = jwtService.getUsuarioPorJwt(token); // Ajusta esto según tu token
                UsuarioContext.setUsuario(usuario); // Configurar el ThreadLocal
                return true;
            } catch (Exception e) {
                UnauthorizedAccessResponse unauthorizedAccessResponse = new UnauthorizedAccessResponse(
                        "ERROR 401: Acceso no autorizado.",
                        System.currentTimeMillis(),
                        HttpStatus.UNAUTHORIZED);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(objectMapper.writeValueAsString(unauthorizedAccessResponse));
                return false;
            }
        }
        else
            return true;
    }
}