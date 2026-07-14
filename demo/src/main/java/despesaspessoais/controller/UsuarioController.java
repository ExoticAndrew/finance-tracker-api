package despesaspessoais.controller;

import despesaspessoais.dtos.AlterarSenhaDTO;
import despesaspessoais.dtos.AtualizarNomeDTO;
import despesaspessoais.dtos.UsuarioResponseDTO;
import despesaspessoais.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> getPerfil() {
        return ResponseEntity.ok(usuarioService.getPerfil());
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> atualizarNome(@Valid @RequestBody AtualizarNomeDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarNome(dto));
    }

    @PutMapping("/me/senha")
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody AlterarSenhaDTO dto) {
        usuarioService.alterarSenha(dto);
        return ResponseEntity.noContent().build();
    }
}