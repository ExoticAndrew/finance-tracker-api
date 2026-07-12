package despesaspessoais.security;

import despesaspessoais.model.Usuario;
import despesaspessoais.repository.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        System.out.println(">>> OAuth2 SUCCESS <<<");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario novo = new Usuario();
                    novo.setEmail(email);
                    novo.setNome(nome != null ? nome : email);
                    novo.setSenha(passwordEncoder.encode(UUID.randomUUID().toString()));
                    return usuarioRepository.save(novo);
                });

        String token = jwtService.gerarToken(usuario.getEmail());

        String frontendUrl =
                "http://localhost:4200/oauth2/callback?token=" + token
                        + "&nome=" + URLEncoder.encode(usuario.getNome(), StandardCharsets.UTF_8);

        System.out.println("Redirecionando para: " + frontendUrl);

        response.sendRedirect(frontendUrl);
    }
}