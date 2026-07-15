package despesaspessoais.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ImportarTransacoesDTO(
        @NotEmpty(message = "A lista de transações não pode estar vazia")
        @Valid
        List<TransacaoRequestDTO> transacoes
) {}