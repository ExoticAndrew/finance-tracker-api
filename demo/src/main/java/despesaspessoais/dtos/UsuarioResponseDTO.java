package despesaspessoais.dtos;

import despesaspessoais.enums.MetodoLogin;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        MetodoLogin metodoLogin
) {}