package despesaspessoais.dtos;

import despesaspessoais.enums.Categoria;

import java.math.BigDecimal;

public record CategoriaResumoDTO(
        Categoria categoria,
        BigDecimal total
) {}