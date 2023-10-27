package msUsers.components.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import msUsers.domain.entities.Colecta;
import msUsers.domain.entities.Donacion;
import msUsers.domain.entities.Usuario;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class NuevaDonacionEvent extends ApplicationEvent {
    private Donacion donacion;
    private Colecta colecta;
    private Usuario usuario;

    public NuevaDonacionEvent(Object source, Donacion donacion, Colecta colecta, Usuario usuario) {
        super(source);
        this.colecta = colecta;
        this.donacion = donacion;
        this.usuario = usuario;
    }
}
