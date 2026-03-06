package com.contabilis.controller;

import com.contabilis.dto.AgendamentoDTO;
import com.contabilis.entity.Agendamento;
import com.contabilis.entity.Servico;
import com.contabilis.entity.Usuario;
import com.contabilis.repository.AgendamentoRepository;
import com.contabilis.repository.ServicoRepository;
import com.contabilis.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoRepository repository;
    private final ServicoRepository servicoRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendamentoController(AgendamentoRepository repository,
                                 ServicoRepository servicoRepository,
                                 UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.servicoRepository = servicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // GET /api/agendamentos — lista todos (ADMIN)
    @GetMapping
    public List<Agendamento> listar() {
        return repository.findAll();
    }

    // GET /api/agendamentos/meus — lista do usuário logado
    @GetMapping("/meus")
    public ResponseEntity<?> meus(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(repository.findByUsuario(usuario));
    }


    @GetMapping("/usuario/{id}")
    public List<Agendamento> listarPorUsuario(@PathVariable Long id) {
        return repository.findByUsuarioId(id);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/agendamentos — cria agendamento com usuário do token JWT
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AgendamentoDTO dto,
                                   Authentication authentication) {
        // Pega o email do token JWT automaticamente
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Agendamento agendamento = Agendamento.builder()
                .usuario(usuario)
                .servico(servico)
                .data(dto.getData())
                .build();

        return ResponseEntity.ok(repository.save(agendamento));
    }

    // PUT /api/agendamentos/{id}/status — atualiza status
    @PutMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(
            @PathVariable Long id,
            @RequestParam Agendamento.Status status) {
        return repository.findById(id).map(a -> {
            a.setStatus(status);
            return ResponseEntity.ok(repository.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/agendamentos/{id} — cancela agendamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        return repository.findById(id).map(a -> {
            a.setStatus(Agendamento.Status.CANCELADO);
            repository.save(a);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}