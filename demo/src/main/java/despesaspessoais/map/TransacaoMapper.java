package despesaspessoais.map;

import despesaspessoais.dtos.TransacaoRequestDTO;
import despesaspessoais.dtos.TransacaoResponseDTO;
import despesaspessoais.model.Transacao;
import org.springframework.stereotype.Component;

@Component
public class TransacaoMapper {

    public Transacao toEntity(TransacaoRequestDTO dto) {
        Transacao transacao = new Transacao();
        transacao.setDescricao(dto.descricao());
        transacao.setTipo(dto.tipo());
        transacao.setData(dto.data());
        transacao.setValor(dto.valor());
        transacao.setCategoria(dto.categoria());
        transacao.setObservacoes(dto.observacoes());

        return transacao;
    }

    public TransacaoResponseDTO toResponseDTO(Transacao transacao){
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getCategoria(),
                transacao.getData(),
                transacao.getObservacoes()
        );
    }
    public void updateEntityFromDTO(TransacaoRequestDTO dto, Transacao transacao){
        transacao.setDescricao(dto.descricao());
        transacao.setTipo(dto.tipo());
        transacao.setData(dto.data());
        transacao.setValor(dto.valor());
        transacao.setCategoria(dto.categoria());
        transacao.setObservacoes(dto.observacoes());

    }
}
