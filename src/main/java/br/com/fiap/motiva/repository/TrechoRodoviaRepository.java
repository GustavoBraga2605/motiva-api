package br.com.fiap.motiva.repository;

import br.com.fiap.motiva.model.TrechoRodovia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrechoRodoviaRepository extends JpaRepository<TrechoRodovia, Integer> {

    // Derived queries: o Spring gera o SQL a partir do NOME do método
    List<TrechoRodovia> findByNivelVegetacaoGreaterThanEqual(Double minimo);

    List<TrechoRodovia> findAllByOrderByNivelVegetacaoDesc();
}
