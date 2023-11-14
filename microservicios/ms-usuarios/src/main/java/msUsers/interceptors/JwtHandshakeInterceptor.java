package msUsers.interceptors;

import jakarta.persistence.EntityManager;
import msUsers.domain.entities.Usuario;
import msUsers.domain.repositories.UsuariosRepository;
import msUsers.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private UsuariosRepository usuariosRepository;

    private EntityManager entityManager;

    public JwtHandshakeInterceptor(UsuariosRepository usuariosRepository, EntityManager entityManager) {
        this.usuariosRepository = usuariosRepository;
        this.entityManager = entityManager;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String idUsuarioEmisor = request.getURI().getQuery();
        idUsuarioEmisor = idUsuarioEmisor.substring(16);
        attributes.put("usuario", idUsuarioEmisor);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
