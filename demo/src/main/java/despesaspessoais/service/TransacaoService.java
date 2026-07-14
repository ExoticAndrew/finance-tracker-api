package despesaspessoais.service;

import despesaspessoais.dtos.*;
import despesaspessoais.exception.TransacaoNotFoundException;
import despesaspessoais.map.TransacaoMapper;
import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;
import despesaspessoais.model.Transacao;
import despesaspessoais.model.Usuario;
import despesaspessoais.repository.TransacaoRepository;
import despesaspessoais.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final TransacaoMapper transacaoMapper;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioAutenticado() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public TransacaoResponseDTO salvar(TransacaoRequestDTO dto) {
        Transacao transacao = transacaoMapper.toEntity(dto);
        transacao.setUsuario(getUsuarioAutenticado());
        return transacaoMapper.toResponseDTO(transacaoRepository.save(transacao));
    }

    public Page<TransacaoResponseDTO> listarTodas(Pageable pageable) {
        return transacaoRepository.findByUsuario(getUsuarioAutenticado(), pageable)
                .map(transacaoMapper::toResponseDTO);
    }

    public TransacaoResponseDTO buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findByIdAndUsuario(id, getUsuarioAutenticado())
                .orElseThrow(() -> new TransacaoNotFoundException(id));
        return transacaoMapper.toResponseDTO(transacao);
    }

    public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO dto) {
        Transacao transacao = transacaoRepository.findByIdAndUsuario(id, getUsuarioAutenticado())
                .orElseThrow(() -> new TransacaoNotFoundException(id));
        transacaoMapper.updateEntityFromDTO(dto, transacao);
        return transacaoMapper.toResponseDTO(transacaoRepository.save(transacao));
    }

    public void deletar(Long id) {
        Transacao transacao = transacaoRepository.findByIdAndUsuario(id, getUsuarioAutenticado())
                .orElseThrow(() -> new TransacaoNotFoundException(id));
        transacaoRepository.delete(transacao);
    }

    public Page<TransacaoResponseDTO> listarPorTipo(Tipotransacao tipo, Pageable pageable) {
        return transacaoRepository.findByTipoAndUsuario(tipo, getUsuarioAutenticado(), pageable)
                .map(transacaoMapper::toResponseDTO);
    }

    public Page<TransacaoResponseDTO> listarPorCategoria(Categoria categoria, Pageable pageable) {
        return transacaoRepository.findByCategoriaAndUsuario(categoria, getUsuarioAutenticado(), pageable)
                .map(transacaoMapper::toResponseDTO);
    }

    public Page<TransacaoResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return transacaoRepository.findByDataBetweenAndUsuario(dataInicio, dataFim, getUsuarioAutenticado(), pageable)
                .map(transacaoMapper::toResponseDTO);
    }

    public BigDecimal calcularTotalPorTipo(Tipotransacao tipo) {
        return transacaoRepository.findByTipoAndUsuario(tipo, getUsuarioAutenticado())
                .stream()
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Page<TransacaoResponseDTO> listarPorTipoEPeriodo(Tipotransacao tipo, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return transacaoRepository.findByTipoAndDataBetweenAndUsuario(tipo, dataInicio, dataFim, getUsuarioAutenticado(), pageable)
                .map(transacaoMapper::toResponseDTO);
    }

    public BigDecimal calcularSaldoAtual() {
        BigDecimal totalReceitas = calcularTotalPorTipo(Tipotransacao.RECEITA);
        BigDecimal totalDespesas = calcularTotalPorTipo(Tipotransacao.DESPESA);
        return totalReceitas.subtract(totalDespesas);
    }

    public List<ResumoMensalDTO> getResumoMensal(int ano) {
        List<Object[]> resultado = transacaoRepository.findResumoMensalPorAno(ano, getUsuarioAutenticado());
        Map<Integer, ResumoMensalDTO> resumoPorMes = new HashMap<>();

        for (Object[] row : resultado) {
            int mes = ((Number) row[0]).intValue();
            Tipotransacao tipo = (Tipotransacao) row[1];
            BigDecimal total = (BigDecimal) row[2];

            resumoPorMes.merge(mes,
                    new ResumoMensalDTO(
                            mes,
                            tipo == Tipotransacao.RECEITA ? total : BigDecimal.ZERO,
                            tipo == Tipotransacao.DESPESA ? total : BigDecimal.ZERO
                    ),
                    (existing, novo) -> new ResumoMensalDTO(
                            mes,
                            tipo == Tipotransacao.RECEITA ? total : existing.totalReceitas(),
                            tipo == Tipotransacao.DESPESA ? total : existing.totalDespesas()
                    )

            );

        }


        return resumoPorMes.values().stream()
                .sorted(Comparator.comparingInt(ResumoMensalDTO::mes))
                .collect(Collectors.toList());
    }
    public List<CategoriaResumoDTO> getRankingCategorias(int ano) {
        List<Object[]> resultado = transacaoRepository.findRankingCategoriasPorAnoETipo(
                Tipotransacao.DESPESA, ano, getUsuarioAutenticado());

        return resultado.stream()
                .map(row -> new CategoriaResumoDTO((Categoria) row[0], (BigDecimal) row[1]))
                .collect(Collectors.toList());
    }

    public ComparativoMensalDTO getComparativoMensal() {
        Usuario usuario = getUsuarioAutenticado();
        LocalDate hoje = LocalDate.now();

        LocalDate inicioMesAtual = hoje.withDayOfMonth(1);
        LocalDate fimMesAtual = hoje;

        LocalDate mesAnterior = hoje.minusMonths(1);
        LocalDate inicioMesAnterior = mesAnterior.withDayOfMonth(1);
        LocalDate fimMesAnterior = mesAnterior.withDayOfMonth(mesAnterior.lengthOfMonth());

        BigDecimal receitaAtual = transacaoRepository.sumValorPorTipoUsuarioEPeriodo(
                Tipotransacao.RECEITA, usuario, inicioMesAtual, fimMesAtual);

        BigDecimal receitaAnterior = transacaoRepository.sumValorPorTipoUsuarioEPeriodo(
                Tipotransacao.RECEITA, usuario, inicioMesAnterior, fimMesAnterior);

        BigDecimal despesaAtual = transacaoRepository.sumValorPorTipoUsuarioEPeriodo(
                Tipotransacao.DESPESA, usuario, inicioMesAtual, fimMesAtual);

        BigDecimal despesaAnterior = transacaoRepository.sumValorPorTipoUsuarioEPeriodo(
                Tipotransacao.DESPESA, usuario, inicioMesAnterior, fimMesAnterior);

        return new ComparativoMensalDTO(
                receitaAtual,
                receitaAnterior,
                despesaAtual,
                despesaAnterior
        );
    }
    public ExtratoDTO getExtrato(int ano) {
        Usuario usuario = getUsuarioAutenticado();
        LocalDate inicioDoAno = LocalDate.of(ano, 1, 1);
        LocalDate fimDoAno = LocalDate.of(ano, 12, 31);
        LocalDate inicioDosTempos = LocalDate.of(2000, 1, 1);

        BigDecimal receitasAntes = transacaoRepository.sumValorPorTipoUsuarioEPeriodo(
                Tipotransacao.RECEITA, usuario, inicioDosTempos, inicioDoAno.minusDays(1));
        BigDecimal despesasAntes = transacaoRepository.sumValorPorTipoUsuarioEPeriodo(
                Tipotransacao.DESPESA, usuario, inicioDosTempos, inicioDoAno.minusDays(1));
        BigDecimal saldoDeArrasto = receitasAntes.subtract(despesasAntes);

        List<TransacaoResponseDTO> transacoes = transacaoRepository
                .findByUsuarioAndDataBetweenOrderByDataAsc(usuario, inicioDoAno, fimDoAno)
                .stream()
                .map(transacaoMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new ExtratoDTO(saldoDeArrasto, transacoes);
    }
    }
