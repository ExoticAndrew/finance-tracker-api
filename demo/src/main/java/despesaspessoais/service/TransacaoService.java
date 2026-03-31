package despesaspessoais.service;

import despesaspessoais.dtos.TransacaoRequestDTO;
import despesaspessoais.dtos.TransacaoResponseDTO;
import despesaspessoais.exception.TransacaoNotFoundException;
import despesaspessoais.map.TransacaoMapper;
import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;
import despesaspessoais.model.Transacao;
import despesaspessoais.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final TransacaoMapper transacaoMapper;

    public TransacaoResponseDTO salvar(TransacaoRequestDTO dto) {
        Transacao transacao = transacaoMapper.toEntity(dto);
        Transacao transacaoSalva = transacaoRepository.save(transacao);
        return transacaoMapper.toResponseDTO(transacaoSalva);
    }

    public Page<TransacaoResponseDTO> listarTodas(Pageable pageable) {
        return transacaoRepository.findAll(pageable)
                .map(transacaoMapper::toResponseDTO);

    }

    public TransacaoResponseDTO buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new TransacaoNotFoundException(id));
        return transacaoMapper.toResponseDTO(transacao);
    }

    public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO dto) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new TransacaoNotFoundException(id));

        transacaoMapper.updateEntityFromDTO(dto, transacao);
        Transacao transacaoAtualizada = transacaoRepository.save(transacao);

        return transacaoMapper.toResponseDTO(transacaoAtualizada);

    }

    public void deletar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new TransacaoNotFoundException(id);
        }
        transacaoRepository.deleteById(id);
    }


    public Page<TransacaoResponseDTO> listarPorTipo(Tipotransacao tipo, Pageable pageable) {
        return transacaoRepository.findByTipo(tipo, pageable)
                .map(transacaoMapper::toResponseDTO);

    }
        public Page<TransacaoResponseDTO> listarPorCategoria(Categoria categoria, Pageable pageable) {
        return transacaoRepository.findByCategoria(categoria, pageable)
                .map(transacaoMapper::toResponseDTO);


    }

    public Page<TransacaoResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return transacaoRepository.findByDataBetween(dataInicio, dataFim, pageable)
                .map(transacaoMapper::toResponseDTO);


    }

    public BigDecimal calcularTotalPorTipo(Tipotransacao tipo) {
        List<Transacao> transacoes = transacaoRepository.findByTipo(tipo);
        return transacoes.stream()
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Page<TransacaoResponseDTO> listarPorTipoEPeriodo(Tipotransacao tipo, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return transacaoRepository.findByTipoAndDataBetween(tipo, dataInicio, dataFim,pageable)
                .map(transacaoMapper::toResponseDTO);

    }


    public BigDecimal calcularSaldoAtual() {
        BigDecimal totalReceitas = calcularTotalPorTipo(Tipotransacao.RECEITA);
        BigDecimal totalDespesas = calcularTotalPorTipo(Tipotransacao.DESPESA);
        return totalReceitas.subtract(totalDespesas);
    }
}

