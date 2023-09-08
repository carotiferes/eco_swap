package msAutenticacion.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import msAutenticacion.domain.entities.Usuario;
import msAutenticacion.domain.model.UsuarioContext;
import msAutenticacion.exceptions.responses.UnauthorizedAccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    JwtService jwtService;

    @Autowired
    AllowedUrlsProvider allowedUrlsProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!Objects.equals(request.getMethod(), "OPTIONS")) {
            String jwtToken = request.getHeader("Authorization");
            String uri = request.getRequestURI();

            AntPathMatcher pathMatcher = new AntPathMatcher();
            List<String> allowedUrls = allowedUrlsProvider.getAllowedUrls();
            for(String allowedUrl : allowedUrls){
                if(pathMatcher.match(allowedUrl, uri))
                    return true;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                log.info("ERROR 401: Acceso no autorizado: " + uri);
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
                        "ERROR 401: Acceso no autorizado: ",
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