package msUsers.components.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.Publicacion;
import msUsers.domain.entities.Usuario;
import msUsers.domain.logistica.enums.EstadoEnvio;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class NuevoEstadoOrdenEnvioEvent extends ApplicationEvent {
    private Usuario usuario;
    private EstadoEnvio estadoEnvio;

    private Publicacion publicacion;

    private Donacion donacion;

    private boolean esPublicacion;

    public NuevoEstadoOrdenEnvioEvent(Object source, Usuario usuario, EstadoEnvio estadoEnvio, Donacion donacion) {
        super(source);
        this.usuario = usuario;
        this.estadoEnvio = estadoEnvio;
        this.donacion = donacion;
        this.esPublicacion = false;
    }

    public NuevoEstadoOrdenEnvioEvent(Object source, Usuario usuario, EstadoEnvio estadoEnvio, Publicacion publicacion) {
        super(source);
        this.usuario = usuario;
        this.estadoEnvio = estadoEnvio;
        this.publicacion = publicacion;
        this.esPublicacion = true;
    }
}
