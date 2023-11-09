package msUsers.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class CustomWebSocketExceptionHandler implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Lógica para manejar el mensaje antes de ser enviado
        log.warn("EL MENSAJE A ENVIAR ES: {}", message.toString());
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        // Lógica para manejar el mensaje después de que se ha enviado
        if (ex != null) {
            // Aquí puedes registrar el error y obtener un stack trace más detallado.
            log.error("Error en WebSocket: " + ex.getMessage(), ex);
        }
    }
}