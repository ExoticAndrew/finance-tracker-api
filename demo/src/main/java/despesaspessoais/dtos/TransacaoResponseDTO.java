package despesaspessoais.dtos;

import despesaspessoais.model.Categoria;
import despesaspessoais.model.Tipotransacao;

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
