package despesaspessoais.dtos;

import java.math.BigDecimal;

public record ResumoFinanceiroDTO(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo
) {
}
