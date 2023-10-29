package msUsers.components.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import msUsers.domain.entities.*;
import msUsers.domain.entities.enums.EstadoDonacion;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class NuevoEstadoDonacionEvent extends ApplicationEvent {
    private Donacion donacion;
    private Colecta colecta;
    private Usuario usuario;
    private EstadoDonacion estadoDonacion;

    public NuevoEstadoDonacionEvent(Object source, Donacion donacion, Colecta colecta, Usuario usuario, EstadoDonacion estadoDonacion) {
        super(source);
        this.estadoDonacion = estadoDonacion;
        this.donacion = donacion;
        this.colecta = colecta;
        this.usuario = usuario;
    }
}
