package despesaspessoais.dtos;

import java.math.BigDecimal;
import java.util.List;

public record ExtratoDTO(
        BigDecimal saldoDeArrasto,
        List<TransacaoResponseDTO> transacoes
) {}