package msAutenticacion.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
                log.error("ERROR 401: Acceso no autorizado: " + uri);
                UnauthorizedAccessResponse unauthorizedAccessResponse = new UnauthorizedAccessResponse(
                        "ERROR 401: Acceso no autorizado",
                        System.currentTimeMillis(),
                        HttpStatus.FORBIDDEN);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(objectMapper.writeValueAsString(unauthorizedAccessResponse));
                return false;
            }

            String token = jwtToken.substring(7);

            try {
                Usuario usuario = jwtService.getUsuarioPorJwt(token); // Ajusta esto según tu token
                UsuarioContext.setUsuario(usuario); // Configurar el ThreadLocal
                this.validateJWT(token, usuario.getSecretJWT());
                return true;
            } catch (Exception e) {
                UnauthorizedAccessResponse unauthorizedAccessResponse = new UnauthorizedAccessResponse(
                        "ERROR 401: Acceso no autorizado",
                        System.currentTimeMillis(),
                        HttpStatus.FORBIDDEN);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(objectMapper.writeValueAsString(unauthorizedAccessResponse));
                return false;
            }
        }
        else
            return true;
    }

    private void validateJWT(String jwtAuth, String secretJWT) {
        try {
            DecodedJWT decode = JWT.decode(jwtAuth);
            Algorithm algorithm = Algorithm.HMAC256(secretJWT);
            log.debug("ISSUER: {}", decode.getIssuer());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(decode.getIssuer())
                    .build();
            //SI TODO SALE BIEN, ENTONCES EL VERIFICADOR NO DEBE SALTAR LA EXCEPCION DE ABAJO.
            verifier.verify(jwtAuth);
        } catch (SignatureVerificationException e) {
            log.error("Error durante la validacion del JWT Token del usuario");
            throw e;
        }
    }
}