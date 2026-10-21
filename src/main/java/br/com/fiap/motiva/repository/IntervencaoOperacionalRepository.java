package br.com.fiap.motiva.repository;

import br.com.fiap.motiva.model.IntervencaoOperacional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IntervencaoOperacionalRepository extends JpaRepository<IntervencaoOperacional, Integer> {

    List<IntervencaoOperacional> findByTrechoId(Integer trechoId);

    List<IntervencaoOperacional> findByDataExecucaoBetween(LocalDate inicio, LocalDate fim);
}
