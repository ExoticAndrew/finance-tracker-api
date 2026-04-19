package despesaspessoais;

import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;
import despesaspessoais.model.Transacao;
import despesaspessoais.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransacaoRepositoryTest {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @BeforeEach
    void setup() {
        transacaoRepository.save(new Transacao(
                null,
                "Mercado",
                new BigDecimal("150.00"),
                Categoria.ALIMENTACAO,
                LocalDate.now(),
                "compras semanais",
                Tipotransacao.DESPESA
        ));
    }

    @Test
    void deveBuscarPorTipo() {
        Page<Transacao> resultado = transacaoRepository.findByTipo(
                Tipotransacao.DESPESA, PageRequest.of(0, 10));

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getTipo()).isEqualTo(Tipotransacao.DESPESA);
    }

    @Test
    void deveBuscarPorCategoria() {
        Page<Transacao> resultado = transacaoRepository.findByCategoria(Categoria.ALIMENTACAO, PageRequest.of(0, 10));
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getCategoria()).isEqualTo(Categoria.ALIMENTACAO);
    }
@Test
    void deveBuscarPorDataEntre() {
        LocalDate hoje = LocalDate.now();
        Page<Transacao> resultado = transacaoRepository.findByDataBetween(hoje.minusDays(1), hoje.plusDays(1), PageRequest.of(0, 10));
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getData()).isEqualTo(hoje);
    }

}
