package msAutenticacion.domain.model;

import lombok.Builder;
import lombok.Data;
import msAutenticacion.domain.entities.Usuario;

@Data
@Builder
public class UsuarioModel {

    private Long id;

    private String username;

    private String telefono;

    private String email;

    public static UsuarioModel crearPerfilRepose(Usuario usuario) {
        return UsuarioModel.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .username(usuario.getUsername())
                .telefono(usuario.getTelefono())
                .build();
    }


}
