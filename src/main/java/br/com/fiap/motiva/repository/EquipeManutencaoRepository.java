package br.com.fiap.motiva.repository;

import br.com.fiap.motiva.model.EquipeManutencao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipeManutencaoRepository extends JpaRepository<EquipeManutencao, Integer> {

    List<EquipeManutencao> findByRegiaoAtendimento(String regiaoAtendimento);

    List<EquipeManutencao> findByNomeEquipeContainingIgnoreCase(String trecho);
}
