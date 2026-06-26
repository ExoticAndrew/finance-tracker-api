package despesaspessoais.service;

import despesaspessoais.dtos.CadastroRequestDTO;
import despesaspessoais.dtos.LoginRequestDTO;
import despesaspessoais.dtos.LoginResponseDTO;
import despesaspessoais.exception.CredenciaisInvalidasException;
import despesaspessoais.exception.EmailJaCadastradoException;
import despesaspessoais.model.Usuario;
import despesaspessoais.repository.UsuarioRepository;
import despesaspessoais.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    // ---------- LOGIN ----------

    @Test
    void login_comCredenciaisValidas_retornaToken() {
        // arrange: finge que o banco achou o usuário e a senha bate
        var dto = new LoginRequestDTO("ana@email.com", "Senha123");
        var usuario = new Usuario();
        usuario.setEmail("ana@email.com");
        usuario.setNome("Ana");
        usuario.setSenha("hash-no-banco");

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Senha123", "hash-no-banco")).thenReturn(true);
        when(jwtService.gerarToken("ana@email.com")).thenReturn("token-fake");

        // act
        LoginResponseDTO resposta = authService.login(dto);

        // assert
        assertThat(resposta.token()).isEqualTo("token-fake");
        assertThat(resposta.nome()).isEqualTo("Ana");
    }

    @Test
    void login_comEmailInexistente_lancaCredenciaisInvalidas() {
        var dto = new LoginRequestDTO("naoexiste@email.com", "Senha123");
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(CredenciaisInvalidasException.class);

        // garante que nem tentou gerar token
        verify(jwtService, never()).gerarToken(anyString());
    }

    @Test
    void login_comSenhaErrada_lancaCredenciaisInvalidas() {
        var dto = new LoginRequestDTO("ana@email.com", "SenhaErrada");
        var usuario = new Usuario();
        usuario.setEmail("ana@email.com");
        usuario.setSenha("hash-no-banco");

        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("SenhaErrada", "hash-no-banco")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    // ---------- CADASTRO ----------

    @Test
    void cadastrar_comEmailNovo_salvaERetornaToken() {
        var dto = new CadastroRequestDTO("Ana", "ana@email.com", "Senha123");

        when(usuarioRepository.existsByEmail("ana@email.com")).thenReturn(false);
        when(passwordEncoder.encode("Senha123")).thenReturn("hash-gerado");
        when(jwtService.gerarToken("ana@email.com")).thenReturn("token-fake");

        LoginResponseDTO resposta = authService.cadastrar(dto);

        assertThat(resposta.token()).isEqualTo("token-fake");
        assertThat(resposta.nome()).isEqualTo("Ana");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void cadastrar_comEmailDuplicado_lancaEmailJaCadastrado() {
        var dto = new CadastroRequestDTO("Ana", "ana@email.com", "Senha123");
        when(usuarioRepository.existsByEmail("ana@email.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.cadastrar(dto))
                .isInstanceOf(EmailJaCadastradoException.class);

        // garante que NÃO salvou nada
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void cadastrar_salvaSenhaComoHash_nuncaEmTextoPuro() {
        var dto = new CadastroRequestDTO("Ana", "ana@email.com", "Senha123");

        when(usuarioRepository.existsByEmail("ana@email.com")).thenReturn(false);
        when(passwordEncoder.encode("Senha123")).thenReturn("hash-gerado");
        when(jwtService.gerarToken(anyString())).thenReturn("token-fake");

        authService.cadastrar(dto);

        // captura o usuário que foi salvo e inspeciona a senha
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario salvo = captor.getValue();
        assertThat(salvo.getSenha()).isEqualTo("hash-gerado");
        assertThat(salvo.getSenha()).isNotEqualTo("Senha123"); // nunca em texto puro
    }
}