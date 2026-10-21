package br.com.fiap.motiva.service;

import br.com.fiap.motiva.exception.RecursoNaoEncontradoException;
import br.com.fiap.motiva.exception.RegraNegocioException;
import br.com.fiap.motiva.model.IntervencaoOperacional;
import br.com.fiap.motiva.repository.IntervencaoOperacionalRepository;
import br.com.fiap.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class IntervencaoOperacionalService {

    private final IntervencaoOperacionalRepository intervencaoRepository;
    private final TrechoRodoviaRepository trechoRepository;

    public IntervencaoOperacionalService(IntervencaoOperacionalRepository intervencaoRepository,
                                         TrechoRodoviaRepository trechoRepository) {
        this.intervencaoRepository = intervencaoRepository;
        this.trechoRepository = trechoRepository;
    }

    public List<IntervencaoOperacional> listar() {
        return intervencaoRepository.findAll();
    }

    public List<IntervencaoOperacional> listarPorTrecho(Integer trechoId) {
        return intervencaoRepository.findByTrechoId(trechoId);
    }

    public IntervencaoOperacional buscarPorId(Integer id) {
        return intervencaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Intervenção " + id + " não encontrada."));
    }

    @Transactional
    public IntervencaoOperacional criar(IntervencaoOperacional intervencao) {
        validar(intervencao);
        intervencao.setId(null);
        return intervencaoRepository.save(intervencao);
    }

    @Transactional
    public IntervencaoOperacional atualizar(Integer id, IntervencaoOperacional dados) {
        IntervencaoOperacional existente = buscarPorId(id);
        validar(dados);
        // Polimorfismo: a subclasse (Pulverizacao / RocadaMecanizada) é definida na criação e não muda depois
        if (!existente.getClass().equals(dados.getClass())) {
            throw new RegraNegocioException(
                    "Não é possível alterar o tipo de serviço de uma intervenção já cadastrada.");
        }
        existente.setTrechoId(dados.getTrechoId());
        if (dados.getDataExecucao() != null) {
            existente.setDataExecucao(dados.getDataExecucao());
        }
        existente.setObservacoes(dados.getObservacoes());
        return intervencaoRepository.save(existente);
    }

    @Transactional
    public void deletar(Integer id) {
        intervencaoRepository.delete(buscarPorId(id));
    }

    private void validar(IntervencaoOperacional i) {
        if (i == null || i.getTrechoId() == null) {
            throw new RegraNegocioException("O trecho da intervenção é obrigatório.");
        }
        if (!trechoRepository.existsById(i.getTrechoId())) {
            throw new RegraNegocioException("O trecho " + i.getTrechoId() + " não existe.");
        }
        if (i.getObservacoes() != null && i.getObservacoes().length() > 255) {
            throw new RegraNegocioException("As observações devem ter no máximo 255 caracteres.");
        }
    }
}
