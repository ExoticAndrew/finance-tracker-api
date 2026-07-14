package despesaspessoais.service;

import despesaspessoais.dtos.AtualizarNomeDTO;
import despesaspessoais.dtos.UsuarioResponseDTO;
import despesaspessoais.model.Usuario;
import despesaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO getPerfil() {
        Usuario usuario = getUsuarioAutenticado();
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public UsuarioResponseDTO atualizarNome(AtualizarNomeDTO dto) {
        Usuario usuario = getUsuarioAutenticado();
        usuario.setNome(dto.nome());
        usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}