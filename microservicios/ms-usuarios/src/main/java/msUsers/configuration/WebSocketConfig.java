package msUsers.configuration;

import lombok.extern.slf4j.Slf4j;
import msUsers.interceptors.JwtHandshakeInterceptor;
import msUsers.services.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatHandler chatHandler;

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/chat-socket")
                .setAllowedOrigins("https://ecoswap.com.ar", "https://www.ecoswap.com.ar")
                .addInterceptors(jwtHandshakeInterceptor);
    }
}
