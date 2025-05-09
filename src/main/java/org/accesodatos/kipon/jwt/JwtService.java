package org.accesodatos.kipon.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.accesodatos.kipon.model.Hucha;
import org.accesodatos.kipon.model.Usuario;
import org.accesodatos.kipon.repository.HuchaRepository;
import org.accesodatos.kipon.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtService {

    private final HuchaRepository huchaRepository;
    private final UsuarioRepository usuarioRepository;
    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long EXPIRATION_TIME = 30 * 60 * 1000;

    public JwtService(HuchaRepository huchaRepository, UsuarioRepository usuarioRepository) {
        this.huchaRepository = huchaRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public String generateToken(String email) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Hucha> huchasAdmin = huchaRepository.findByAdministradorId(usuario.getId());
        List<String> roles = huchasAdmin.stream()
                .map(hucha -> "ADMIN_" + hucha.getId())
                .toList();

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("roles", roles)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extraerEmail(String token) {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            return parseClaims(token, key).getBody().getSubject();
    }

    public boolean esTokenValido (String token) {
        try {
            extraerEmail(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token, SecretKey key) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

}
