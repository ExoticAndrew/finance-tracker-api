package despesaspessoais.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "transacoes")


public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private Categoria categoria;

    @Column (nullable = false)
    private LocalDate data;

    @Column (length = 500)
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipotransacao tipo;
}
