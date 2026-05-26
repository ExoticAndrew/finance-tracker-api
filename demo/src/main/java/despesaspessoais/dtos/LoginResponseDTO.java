package despesaspessoais.dtos;

public record LoginResponseDTO(
        String token,
        String nome,
        String email
) {}