package com.contabilis.repository;

import com.contabilis.entity.Agendamento;
import com.contabilis.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuario(Usuario usuario);
    List<Agendamento> findByUsuarioId(Long id);
}
