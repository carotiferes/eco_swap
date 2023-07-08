package msUsers.domain.model;

import lombok.Builder;
import lombok.Data;
import msUsers.domain.entities.Perfil;

@Data
@Builder
public class PerfilModel {

    private Long id;

    private String username;

    private String telefono;

    private String email;

    public static PerfilModel crearPerfilRepose(Perfil perfil) {
        return PerfilModel.builder()
                .id(perfil.getIdPerfil())
                .email(perfil.getEmail())
                .username(perfil.getUsername())
                .telefono(perfil.getTelefono())
                .build();
    }


}
