package despesaspessoais.dtos;

import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequestDTO(

        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 3, max = 100, message = "A descrição deve ter entre 3 e 100 caracteres")
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "O tipo é obrigatório")
        Tipotransacao tipo,

        @NotNull(message = "A categoria é obrigatória")
        Categoria categoria,

        @NotNull(message = "A data é obrigatória")
        @PastOrPresent(message = "A data não pode ser futura")
        LocalDate data,

        @Size(max = 500, message = "As observações não podem ter mais de 500 caracteres")
        String observacoes
) {
}
