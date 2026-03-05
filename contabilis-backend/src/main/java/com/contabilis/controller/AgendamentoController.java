package com.contabilis.controller;

import com.contabilis.entity.Agendamento;
import com.contabilis.repository.AgendamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoRepository repository;

    public AgendamentoController(AgendamentoRepository repository) {
        this.repository = repository;
    }

    // GET /api/agendamentos
    @GetMapping
    public List<Agendamento> listar() {
        return repository.findAll();
    }

    // GET /api/agendamentos/usuario/{id}
    @GetMapping("/usuario/{id}")
    public List<Agendamento> listarPorUsuario(@PathVariable Long id) {
        return repository.findByUsuarioId(id);
    }

    // GET /api/agendamentos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/agendamentos
    @PostMapping
    public Agendamento criar(@RequestBody Agendamento agendamento) {
        return repository.save(agendamento);
    }

    // PUT /api/agendamentos/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(
            @PathVariable Long id,
            @RequestParam Agendamento.Status status) {
        return repository.findById(id).map(a -> {
            a.setStatus(status);
            return ResponseEntity.ok(repository.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/agendamentos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        return repository.findById(id).map(a -> {
            a.setStatus(Agendamento.Status.CANCELADO);
            repository.save(a);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
