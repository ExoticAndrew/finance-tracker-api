package despesaspessoais.service;

import despesaspessoais.dtos.CadastroRequestDTO;
import despesaspessoais.dtos.LoginRequestDTO;
import despesaspessoais.dtos.LoginResponseDTO;
import despesaspessoais.enums.MetodoLogin;
import despesaspessoais.exception.CredenciaisInvalidasException;
import despesaspessoais.exception.EmailJaCadastradoException;
import despesaspessoais.model.Usuario;
import despesaspessoais.repository.UsuarioRepository;
import despesaspessoais.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        String token = jwtService.gerarToken(usuario.getEmail());
        return new LoginResponseDTO(token, usuario.getNome());
    }

    public LoginResponseDTO cadastrar(CadastroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailJaCadastradoException();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setMetodoLogin(MetodoLogin.LOCAL);

        usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario.getEmail());
        return new LoginResponseDTO(token, usuario.getNome());
    }
}