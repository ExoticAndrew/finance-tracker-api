package despesaspessoais.dtos;

import java.math.BigDecimal;

public record ResumoMensalDTO(
        int mes,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas
) {}