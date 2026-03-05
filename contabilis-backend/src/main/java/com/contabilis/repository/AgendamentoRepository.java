package com.contabilis.repository;

import com.contabilis.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuarioId(Long usuarioId);
    List<Agendamento> findByStatus(Agendamento.Status status);
}
