package com.contabilis.repository;

import com.contabilis.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByUsuarioId(Long usuarioId);
    List<Pagamento> findByStatus(Pagamento.Status status);
}
