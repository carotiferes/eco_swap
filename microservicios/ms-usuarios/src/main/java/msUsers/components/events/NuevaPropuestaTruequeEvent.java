package msUsers.components.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.Trueque;
import msUsers.domain.entities.Usuario;
import org.springframework.context.ApplicationEvent;


@Data
@EqualsAndHashCode(callSuper = true)
public class NuevaPropuestaTruequeEvent extends ApplicationEvent {

    private Usuario usuario;
    private Trueque trueque;
    private Publicacion publicacionOrigen;

    public NuevaPropuestaTruequeEvent(Object source, Trueque trueque, Usuario usuario, Publicacion publicacionOrigen) {
        super(source);
        this.usuario = usuario;
        this.trueque = trueque;
        this.publicacionOrigen = publicacionOrigen;
    }
}


