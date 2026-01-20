package despesaspessoais.service;

import despesaspessoais.dtos.TransacaoRequestDTO;
import despesaspessoais.dtos.TransacaoResponseDTO;
import despesaspessoais.exception.TransacaoNotFoundException;
import despesaspessoais.map.TransacaoMapper;
import despesaspessoais.model.Categoria;
import despesaspessoais.model.Tipotransacao;
import despesaspessoais.model.Transacao;
import despesaspessoais.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<TransacaoResponseDTO> listarTodas() {
        return transacaoRepository.findAll()
                .stream()
                .map(transacaoMapper::toResponseDTO)
                .collect(Collectors.toList());
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


    public List<TransacaoResponseDTO> listarPorTipo(Tipotransacao tipo) {
        return transacaoRepository.findByTipo(tipo)
                .stream()
                .map(transacaoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    public List<TransacaoResponseDTO> listarPorCategoria(Categoria categoria) {
        return transacaoRepository.findByCategoria(categoria)
                .stream()
                .map(transacaoMapper::toResponseDTO)
                .collect(Collectors.toList());

    }

    public List<TransacaoResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return transacaoRepository.findByDataBetween(dataInicio, dataFim)
                .stream()
                .map(transacaoMapper::toResponseDTO)
                .collect(Collectors.toList());

    }

    public BigDecimal calcularTotalPorTipo(Tipotransacao tipo) {
        List<Transacao> transacoes = transacaoRepository.findByTipo(tipo);
        return transacoes.stream()
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<TransacaoResponseDTO> listarPorTipoEPeriodo(Tipotransacao tipo, LocalDate dataInicio, LocalDate dataFim) {
        return transacaoRepository.findByTipoAndDataBetween(tipo, dataInicio, dataFim)
                .stream()
                .map(transacaoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    public BigDecimal calcularSaldoAtual() {
        BigDecimal totalReceitas = calcularTotalPorTipo(Tipotransacao.RECEITA);
        BigDecimal totalDespesas = calcularTotalPorTipo(Tipotransacao.DESPESA);
        return totalReceitas.subtract(totalDespesas);
    }
}

