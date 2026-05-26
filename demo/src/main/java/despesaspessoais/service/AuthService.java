package despesaspessoais.service;

import despesaspessoais.dtos.CadastroRequestDTO;
import despesaspessoais.dtos.LoginRequestDTO;
import despesaspessoais.dtos.LoginResponseDTO;
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
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        String token = jwtService.gerarToken(usuario.getEmail());

        return new LoginResponseDTO(token, usuario.getNome(), usuario.getEmail());
    }

    public LoginResponseDTO cadastrar(CadastroRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario(
                null,
                dto.nome(),
                dto.email(),
                passwordEncoder.encode(dto.senha())
        );

        usuarioRepository.save(usuario);

        String token = jwtService.gerarToken(usuario.getEmail());

        return new LoginResponseDTO(token, usuario.getNome(), usuario.getEmail());
    }
}