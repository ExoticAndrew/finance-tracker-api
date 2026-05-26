package despesaspessoais.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String email,
        @NotBlank String senha
) {}