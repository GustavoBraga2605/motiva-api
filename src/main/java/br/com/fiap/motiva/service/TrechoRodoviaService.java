package br.com.fiap.motiva.service;

import br.com.fiap.motiva.exception.RecursoNaoEncontradoException;
import br.com.fiap.motiva.exception.RegraNegocioException;
import br.com.fiap.motiva.model.TrechoRodovia;
import br.com.fiap.motiva.repository.IntervencaoOperacionalRepository;
import br.com.fiap.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrechoRodoviaService {

    private final TrechoRodoviaRepository trechoRepository;
    private final IntervencaoOperacionalRepository intervencaoRepository;

    public TrechoRodoviaService(TrechoRodoviaRepository trechoRepository,
                                IntervencaoOperacionalRepository intervencaoRepository) {
        this.trechoRepository = trechoRepository;
        this.intervencaoRepository = intervencaoRepository;
    }

    public List<TrechoRodovia> listar() {
        return trechoRepository.findAll();
    }

    public List<TrechoRodovia> listarComVegetacaoMinima(Double minimo) {
        if (minimo == null || minimo < 0) {
            throw new RegraNegocioException("O valor mínimo de vegetação deve ser maior ou igual a zero.");
        }
        return trechoRepository.findByNivelVegetacaoGreaterThanEqual(minimo);
    }

    public TrechoRodovia buscarPorId(Integer id) {
        return trechoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Trecho " + id + " não encontrado."));
    }

    @Transactional
    public TrechoRodovia criar(TrechoRodovia trecho) {
        validar(trecho);
        trecho.setId(null); // garante INSERT: o id é gerado pelo banco, nunca pelo cliente
        return trechoRepository.save(trecho);
    }

    @Transactional
    public TrechoRodovia atualizar(Integer id, TrechoRodovia dados) {
        TrechoRodovia existente = buscarPorId(id);
        validar(dados);
        existente.setQuilometroInicial(dados.getQuilometroInicial());
        existente.setQuilometroFinal(dados.getQuilometroFinal());
        existente.setNivelVegetacao(dados.getNivelVegetacao());
        return trechoRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        TrechoRodovia existente = buscarPorId(id);
        // Sem essa checagem o Oracle recusaria o DELETE (FK em INTERVENCAO_OPERACIONAL) com um erro técnico
        if (!intervencaoRepository.findByTrechoId(id).isEmpty()) {
            throw new RegraNegocioException(
                    "Não é possível remover o trecho " + id + ": existem intervenções vinculadas a ele.");
        }
        trechoRepository.delete(existente);
    }

    // Regra da Sprint 1/2: registrar crescimento da vegetação
    @Transactional
    public TrechoRodovia registrarCrescimento(Integer id, Double taxa) {
        if (taxa == null || taxa <= 0) {
            throw new RegraNegocioException("A taxa de crescimento deve ser maior que zero.");
        }
        TrechoRodovia trecho = buscarPorId(id);
        trecho.registrarCrescimento(taxa);
        return trechoRepository.save(trecho);
    }

    private void validar(TrechoRodovia t) {
        if (t == null || t.getQuilometroInicial() == null || t.getQuilometroFinal() == null
                || t.getNivelVegetacao() == null) {
            throw new RegraNegocioException(
                    "Quilômetro inicial, quilômetro final e nível de vegetação são obrigatórios.");
        }
        if (t.getQuilometroInicial() < 0) {
            throw new RegraNegocioException("O quilômetro inicial não pode ser negativo.");
        }
        if (t.getQuilometroFinal() <= t.getQuilometroInicial()) {
            throw new RegraNegocioException("O quilômetro final deve ser maior que o quilômetro inicial.");
        }
        if (t.getNivelVegetacao() < 0) {
            throw new RegraNegocioException("O nível de vegetação não pode ser negativo.");
        }
    }
}
