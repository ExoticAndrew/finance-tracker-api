package despesaspessoais.controller;

import despesaspessoais.dtos.TransacaoRequestDTO;
import despesaspessoais.dtos.TransacaoResponseDTO;
import despesaspessoais.model.Categoria;
import despesaspessoais.model.Tipotransacao;
import despesaspessoais.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TransacaoResponseDTO>atualizar(@PathVariable Long id,@Valid @RequestBody TransacaoRequestDTO dto){
        TransacaoResponseDTO transacaoAtualizada = transacaoService.atualizar(id, dto);
        return ResponseEntity.ok(transacaoAtualizada);
    }
    @DeleteMapping ("/{id}")
    public ResponseEntity<Void>deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listarTodas() {
        List<TransacaoResponseDTO> transacoes = transacaoService.listarTodas();
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping ("/categoria/{categoria}")
    public ResponseEntity<List<TransacaoResponseDTO>>listarPorCategoria(@PathVariable Categoria categoria){
        List<TransacaoResponseDTO> transacoes = transacaoService.listarPorCategoria(categoria);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping ("/tipo/{tipo}")
    public ResponseEntity<List<TransacaoResponseDTO>>listarPorTipo(@PathVariable Tipotransacao tipo){
        List<TransacaoResponseDTO> transacoes = transacaoService.listarPorTipo(tipo);
        return ResponseEntity.ok(transacoes);
    }
@GetMapping("/periodo")
    public ResponseEntity<List<TransacaoResponseDTO>>listarPorPeriodo(@RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim) {
    List<TransacaoResponseDTO> transacoes = transacaoService.listarPorPeriodo(dataInicio, dataFim);
    return ResponseEntity.ok(transacoes);
}
@GetMapping ("/saldo")
    public ResponseEntity<BigDecimal> calcularSaldoAtual() {
        BigDecimal saldo = transacaoService.calcularSaldoAtual();
        return ResponseEntity.ok(saldo);
}
@GetMapping ("/total/tipo/{tipo}")
    public ResponseEntity<BigDecimal>calcularTotalPorTipo(@PathVariable Tipotransacao tipo){
        BigDecimal total = transacaoService.calcularTotalPorTipo(tipo);
        return  ResponseEntity.ok(total);
}
}
