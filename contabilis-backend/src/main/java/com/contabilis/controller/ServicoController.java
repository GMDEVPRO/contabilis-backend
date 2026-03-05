package com.contabilis.controller;

import com.contabilis.entity.Servico;
import com.contabilis.repository.ServicoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoRepository repository;

    public ServicoController(ServicoRepository repository) {
        this.repository = repository;
    }

    // GET /api/servicos — público
    @GetMapping
    public List<Servico> listar() {
        return repository.findByAtivoTrue();
    }

    // GET /api/servicos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/servicos — só admin
    @PostMapping
    public Servico criar(@RequestBody Servico servico) {
        return repository.save(servico);
    }

    // PUT /api/servicos/{id} — só admin
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @RequestBody Servico dados) {
        return repository.findById(id).map(s -> {
            s.setTitulo(dados.getTitulo());
            s.setDescricao(dados.getDescricao());
            s.setPreco(dados.getPreco());
            s.setAtivo(dados.getAtivo());
            return ResponseEntity.ok(repository.save(s));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/servicos/{id} — só admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return repository.findById(id).map(s -> {
            s.setAtivo(false); // soft delete
            repository.save(s);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
