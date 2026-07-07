package despesaspessoais.dtos;

import java.math.BigDecimal;

public record ComparativoMensalDTO(
        BigDecimal receitaAtual,
        BigDecimal receitaAnterior,
        BigDecimal despesaAtual,
        BigDecimal despesaAnterior
) {}