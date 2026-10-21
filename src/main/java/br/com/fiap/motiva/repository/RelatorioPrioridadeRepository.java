package br.com.fiap.motiva.repository;

import br.com.fiap.motiva.model.RelatorioPrioridade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioPrioridadeRepository extends JpaRepository<RelatorioPrioridade, Integer> {

    // Base do endpoint GET /api/relatorios/periodo?inicio=...&fim=...
    List<RelatorioPrioridade> findByDataGeracaoBetween(LocalDate inicio, LocalDate fim);

    List<RelatorioPrioridade> findAllByOrderByDataGeracaoDesc();
}
