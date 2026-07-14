package despesaspessoais.service;

import despesaspessoais.dtos.AlterarSenhaDTO;
import despesaspessoais.dtos.AtualizarNomeDTO;
import despesaspessoais.dtos.UsuarioResponseDTO;
import despesaspessoais.enums.MetodoLogin;
import despesaspessoais.exception.CredenciaisInvalidasException;
import despesaspessoais.model.Usuario;
import despesaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO getPerfil() {
        Usuario usuario = getUsuarioAutenticado();
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), metodoOuPadrao(usuario));
    }

    public UsuarioResponseDTO atualizarNome(AtualizarNomeDTO dto) {
        Usuario usuario = getUsuarioAutenticado();
        usuario.setNome(dto.nome());
        usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), metodoOuPadrao(usuario));
    }

    public void alterarSenha(AlterarSenhaDTO dto) {
        Usuario usuario = getUsuarioAutenticado();

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }

    private MetodoLogin metodoOuPadrao(Usuario usuario) {
        return usuario.getMetodoLogin() != null ? usuario.getMetodoLogin() : MetodoLogin.LOCAL;
    }
}