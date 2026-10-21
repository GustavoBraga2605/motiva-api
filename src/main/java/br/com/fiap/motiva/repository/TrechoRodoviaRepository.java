package br.com.fiap.motiva.repository;

import br.com.fiap.motiva.model.TrechoRodovia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrechoRodoviaRepository extends JpaRepository<TrechoRodovia, Integer> {

    List<TrechoRodovia> findByNivelVegetacaoGreaterThanEqual(Double minimo);

    List<TrechoRodovia> findAllByOrderByNivelVegetacaoDesc();
}
