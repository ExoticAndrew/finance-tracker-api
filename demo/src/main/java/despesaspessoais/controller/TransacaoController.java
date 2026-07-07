package despesaspessoais.controller;

import despesaspessoais.dtos.ComparativoMensalDTO;
import despesaspessoais.dtos.ResumoMensalDTO;
import despesaspessoais.dtos.TransacaoRequestDTO;
import despesaspessoais.dtos.TransacaoResponseDTO;
import despesaspessoais.enums.Categoria;
import despesaspessoais.enums.Tipotransacao;
import despesaspessoais.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO transacaoCriada = transacaoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoCriada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        TransacaoResponseDTO transacao = transacaoService.buscarPorId(id);
        return ResponseEntity.ok(transacao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO transacaoAtualizada = transacaoService.atualizar(id, dto);
        return ResponseEntity.ok(transacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Page<TransacaoResponseDTO>> listarPorCategoria(@PathVariable Categoria categoria, @PageableDefault(size = 10, sort = "data") Pageable pageable) {
        Page<TransacaoResponseDTO> transacoes = transacaoService.listarPorCategoria(categoria, pageable);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<TransacaoResponseDTO>> listarPorTipo(@PathVariable Tipotransacao tipo, @PageableDefault(size = 10, sort = "data") Pageable pageable) {
        Page<TransacaoResponseDTO> transacoes = transacaoService.listarPorTipo(tipo, pageable);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<TransacaoResponseDTO>> listarPorPeriodo(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PageableDefault(size = 10, sort = "data") Pageable pageable) {
        Page<TransacaoResponseDTO> transacoes = transacaoService.listarPorPeriodo(dataInicio, dataFim, pageable);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> calcularSaldoAtual() {
        BigDecimal saldo = transacaoService.calcularSaldoAtual();
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/total/tipo/{tipo}")
    public ResponseEntity<BigDecimal> calcularTotalPorTipo(@PathVariable Tipotransacao tipo) {
        BigDecimal total = transacaoService.calcularTotalPorTipo(tipo);
        return ResponseEntity.ok(total);
    }
    @GetMapping
    public ResponseEntity<Page<TransacaoResponseDTO>> listar(
            @PageableDefault(size = 10, sort = "data", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<TransacaoResponseDTO> transacoes = transacaoService.listarTodas(pageable);
        return ResponseEntity.ok(transacoes);
    }
    @GetMapping("/resumo/mensal")
    public ResponseEntity<List<ResumoMensalDTO>> getResumoMensal(@RequestParam int ano) {
        return ResponseEntity.ok(transacaoService.getResumoMensal(ano));
    }
    @GetMapping("/comparativo-mensal")
    public ResponseEntity<ComparativoMensalDTO> getComparativoMensal() {
        return ResponseEntity.ok(transacaoService.getComparativoMensal());
    }
}
