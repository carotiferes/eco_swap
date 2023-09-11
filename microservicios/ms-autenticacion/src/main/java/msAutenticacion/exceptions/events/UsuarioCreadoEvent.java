package msAutenticacion.exceptions.events;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
import msAutenticacion.domain.entities.Usuario;

public class UsuarioCreadoEvent extends ApplicationEvent {

    private final Usuario usuarioCreado;

    public UsuarioCreadoEvent(Object source, Usuario usuarioCreado) {
        super(source);
        this.usuarioCreado = usuarioCreado;
    }

    public Usuario getUsuarioCreado() {
        return usuarioCreado;
    }
}
