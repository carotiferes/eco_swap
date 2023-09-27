package msUsers.domain.model;

import msUsers.domain.entities.Usuario;


public class UsuarioContext {
    private static final ThreadLocal<Usuario> usuario = new ThreadLocal<>();

    public static Usuario getUsuario() {
        return usuario.get();
    }
    public static void setUsuario(Usuario user) { usuario.set(user);}
}
