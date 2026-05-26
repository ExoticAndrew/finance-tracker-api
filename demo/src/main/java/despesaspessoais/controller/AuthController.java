package despesaspessoais.controller;

import despesaspessoais.dtos.CadastroRequestDTO;
import despesaspessoais.dtos.LoginRequestDTO;
import despesaspessoais.dtos.LoginResponseDTO;
import despesaspessoais.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<LoginResponseDTO> cadastrar(@Valid @RequestBody CadastroRequestDTO dto) {
        LoginResponseDTO response = authService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}