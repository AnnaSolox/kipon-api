package org.accesodatos.kipon.config.security;

import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    // Método para obtener el email del usuario desde el contexto de seguridad
    public String obtenerEmailUsuarioDesdeContexto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            // Aquí se asume que UsuarioPrincipal tiene el email
            return ((Usuario) authentication.getPrincipal()).getEmail();
        }
        return null; // O lanzar una excepción si no está autenticado
    }

    // Método para verificar si el usuario es administrador de la hucha
    public boolean esAdministradorDeHucha(Hucha hucha, String emailUsuario) {
        return hucha.getUsuarios().stream()
                .anyMatch(usuarioHucha -> usuarioHucha.getUsuario().getEmail().equals(emailUsuario) && "Administrador".equals(usuarioHucha.getRol()));
    }

}
