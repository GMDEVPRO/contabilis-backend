package com.contabilis.controller;

import com.contabilis.entity.Pagamento;
import com.contabilis.repository.PagamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoRepository repository;

    public PagamentoController(PagamentoRepository repository) {
        this.repository = repository;
    }

    // GET /api/pagamentos
    @GetMapping
    public List<Pagamento> listar() {
        return repository.findAll();
    }

    // GET /api/pagamentos/usuario/{id}
    @GetMapping("/usuario/{id}")
    public List<Pagamento> listarPorUsuario(@PathVariable Long id) {
        return repository.findByUsuarioId(id);
    }

    // GET /api/pagamentos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/pagamentos
    @PostMapping
    public Pagamento criar(@RequestBody Pagamento pagamento) {
        return repository.save(pagamento);
    }

    // PUT /api/pagamentos/{id}/aprovar
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Pagamento> aprovar(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            p.setStatus(Pagamento.Status.APROVADO);
            p.setPagoEm(LocalDateTime.now());
            return ResponseEntity.ok(repository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/pagamentos/{id}/estornar
    @PutMapping("/{id}/estornar")
    public ResponseEntity<Pagamento> estornar(@PathVariable Long id) {
        return repository.findById(id).map(p -> {
            p.setStatus(Pagamento.Status.ESTORNADO);
            return ResponseEntity.ok(repository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }
}
