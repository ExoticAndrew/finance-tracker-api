package despesaspessoais.repository;

import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;
import despesaspessoais.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByTipo(Tipotransacao tipo);

    List<Transacao> findByCategoria(Categoria categoria);

    List<Transacao> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    List<Transacao> findByTipoAndDataBetween(Tipotransacao tipo, LocalDate dataInicio, LocalDate dataFim);

    List<Transacao> findByCategoriaAndDataBetween(Categoria categoria, LocalDate dataInicio, LocalDate dataFim);

    @Query("SELECT SUM(t.valor) FROM Transacao t WHERE t.categoria = :categoria AND t.data BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTotalPorCategoriaEPeriodo(
            @Param("categoria") Categoria categoria,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );
}
