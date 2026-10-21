package br.com.fiap.motiva.service;

import br.com.fiap.motiva.exception.RecursoNaoEncontradoException;
import br.com.fiap.motiva.exception.RegraNegocioException;
import br.com.fiap.motiva.model.EquipeManutencao;
import br.com.fiap.motiva.repository.EquipeManutencaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EquipeManutencaoService {

    private final EquipeManutencaoRepository equipeRepository;

    public EquipeManutencaoService(EquipeManutencaoRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    public List<EquipeManutencao> listar() {
        return equipeRepository.findAll();
    }

    public List<EquipeManutencao> listarPorRegiao(String regiao) {
        if (regiao == null || regiao.isBlank()) {
            throw new RegraNegocioException("A região de atendimento é obrigatória para a busca.");
        }
        return equipeRepository.findByRegiaoAtendimento(regiao);
    }

    public EquipeManutencao buscarPorId(Integer id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe " + id + " não encontrada."));
    }

    @Transactional
    public EquipeManutencao criar(EquipeManutencao equipe) {
        validar(equipe);
        equipe.setId(null);
        return equipeRepository.save(equipe);
    }

    @Transactional
    public EquipeManutencao atualizar(Integer id, EquipeManutencao dados) {
        EquipeManutencao existente = buscarPorId(id);
        validar(dados);
        existente.setNomeEquipe(dados.getNomeEquipe());
        existente.setQuantidadeFuncionarios(dados.getQuantidadeFuncionarios());
        existente.setRegiaoAtendimento(dados.getRegiaoAtendimento());
        return equipeRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        equipeRepository.delete(buscarPorId(id));
    }

    private void validar(EquipeManutencao e) {
        if (e == null || e.getNomeEquipe() == null || e.getNomeEquipe().isBlank()) {
            throw new RegraNegocioException("O nome da equipe é obrigatório.");
        }
        if (e.getNomeEquipe().length() > 100) {
            throw new RegraNegocioException("O nome da equipe deve ter no máximo 100 caracteres.");
        }
        if (e.getQuantidadeFuncionarios() == null || e.getQuantidadeFuncionarios() <= 0) {
            throw new RegraNegocioException("A quantidade de funcionários deve ser maior que zero.");
        }
        if (e.getRegiaoAtendimento() == null || e.getRegiaoAtendimento().isBlank()) {
            throw new RegraNegocioException("A região de atendimento é obrigatória.");
        }
        if (e.getRegiaoAtendimento().length() > 100) {
            throw new RegraNegocioException("A região de atendimento deve ter no máximo 100 caracteres.");
        }
    }
}
