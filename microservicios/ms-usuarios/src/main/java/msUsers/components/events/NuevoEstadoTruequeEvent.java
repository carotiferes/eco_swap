package msUsers.components.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoTrueque;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class NuevoEstadoTruequeEvent extends ApplicationEvent {
    private Trueque trueque;
    private Usuario usuario;
    private Publicacion publicacion;
    private EstadoTrueque estadoTrueque;
    public NuevoEstadoTruequeEvent(Object source, Trueque trueque, Usuario usuario, Publicacion publicacion, EstadoTrueque estadoTrueque) {
        super(source);
        this.trueque = trueque;
        this.usuario = usuario;
        this.publicacion = publicacion;
        this.estadoTrueque = estadoTrueque;
    }
}
