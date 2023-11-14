package msUsers.components.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import msUsers.domain.entities.Compra;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.Usuario;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class NuevaCompraEvent extends ApplicationEvent {
    private Compra compra;
    private Usuario usuario;
    private Publicacion publicacion;

    public NuevaCompraEvent(Object source, Compra compra, Usuario usuario, Publicacion publicacion) {
        super(source);
        this.publicacion = publicacion;
        this.compra = compra;
        this.usuario = usuario;
    }
}
