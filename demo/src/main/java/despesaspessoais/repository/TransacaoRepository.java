package despesaspessoais.repository;

import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;
import despesaspessoais.model.Transacao;
import despesaspessoais.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    Page<Transacao> findByUsuario(Usuario usuario, Pageable pageable);

    Optional<Transacao> findByIdAndUsuario(Long id, Usuario usuario);

    Page<Transacao> findByTipoAndUsuario(Tipotransacao tipo, Usuario usuario, Pageable pageable);

    Page<Transacao> findByCategoriaAndUsuario(Categoria categoria, Usuario usuario, Pageable pageable);

    Page<Transacao> findByDataBetweenAndUsuario(LocalDate dataInicio, LocalDate dataFim, Usuario usuario, Pageable pageable);

    Page<Transacao> findByTipoAndDataBetweenAndUsuario(Tipotransacao tipo, LocalDate dataInicio, LocalDate dataFim, Usuario usuario, Pageable pageable);

    List<Transacao> findByTipoAndUsuario(Tipotransacao tipo, Usuario usuario);

    @Query("SELECT SUM(t.valor) FROM Transacao t WHERE t.categoria = :categoria AND t.data BETWEEN :dataInicio AND :dataFim AND t.usuario = :usuario")
    BigDecimal calcularTotalPorCategoriaEPeriodo(
            @Param("categoria") Categoria categoria,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("usuario") Usuario usuario
    );

    @Query("SELECT MONTH(t.data) as mes, t.tipo, SUM(t.valor) as total " +
            "FROM Transacao t " +
            "WHERE YEAR(t.data) = :ano AND t.usuario = :usuario " +
            "GROUP BY MONTH(t.data), t.tipo " +
            "ORDER BY MONTH(t.data)")
    List<Object[]> findResumoMensalPorAno(@Param("ano") int ano, @Param("usuario") Usuario usuario);
}