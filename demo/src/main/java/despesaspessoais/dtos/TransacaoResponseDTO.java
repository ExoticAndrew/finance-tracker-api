package despesaspessoais.dtos;

import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        Tipotransacao tipo,
        Categoria categoria,
        LocalDate data,
        String observacoes
) {
}
