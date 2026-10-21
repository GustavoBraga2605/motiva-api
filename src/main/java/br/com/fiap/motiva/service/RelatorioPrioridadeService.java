package br.com.fiap.motiva.service;

import br.com.fiap.motiva.exception.RegraNegocioException;
import br.com.fiap.motiva.model.NivelPrioridade;
import br.com.fiap.motiva.model.RelatorioPrioridade;
import br.com.fiap.motiva.model.TrechoRodovia;
import br.com.fiap.motiva.repository.RelatorioPrioridadeRepository;
import br.com.fiap.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Motor de prioridade (antigo GeradorRelatorio). Mesma regra da Sprint 2/3,
 * mas sem System.out: devolve o relatório para o Controller virar JSON.
 */
@Service
@Transactional(readOnly = true)
public class RelatorioPrioridadeService {

    // Limiares da Sprint 2: ALTA >= 80 cm, MÉDIA de 40 a 79,99 cm, BAIXA < 40 cm
    private static final double LIMIAR_ALTA = 80;
    private static final double LIMIAR_MEDIA = 40;

    private final TrechoRodoviaRepository trechoRepository;
    private final RelatorioPrioridadeRepository relatorioRepository;

    public RelatorioPrioridadeService(TrechoRodoviaRepository trechoRepository,
                                      RelatorioPrioridadeRepository relatorioRepository) {
        this.trechoRepository = trechoRepository;
        this.relatorioRepository = relatorioRepository;
    }

    public NivelPrioridade classificar(double nivelVegetacao) {
        if (nivelVegetacao >= LIMIAR_ALTA) {
            return NivelPrioridade.ALTA;
        }
        if (nivelVegetacao >= LIMIAR_MEDIA) {
            return NivelPrioridade.MEDIA;
        }
        return NivelPrioridade.BAIXA;
    }

    @Transactional
    public RelatorioPrioridade gerarRelatorio() {
        List<TrechoRodovia> trechos = trechoRepository.findAll();
        if (trechos.isEmpty()) {
            throw new RegraNegocioException("Não há trechos cadastrados para gerar o relatório.");
        }

        int qtdAlta = 0;
        int qtdMedia = 0;
        int qtdBaixa = 0;
        for (TrechoRodovia trecho : trechos) {
            switch (classificar(trecho.getNivelVegetacao())) {
                case ALTA -> qtdAlta++;
                case MEDIA -> qtdMedia++;
                case BAIXA -> qtdBaixa++;
            }
        }

        String resumo = qtdAlta + " trecho(s) em prioridade ALTA, "
                + qtdMedia + " em MÉDIA e " + qtdBaixa + " em BAIXA.";

        return relatorioRepository.save(
                new RelatorioPrioridade(LocalDate.now(), qtdAlta, qtdMedia, qtdBaixa, resumo));
    }

    public List<RelatorioPrioridade> listarHistorico() {
        return relatorioRepository.findAllByOrderByDataGeracaoDesc();
    }

    public List<RelatorioPrioridade> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("Informe as datas de início e fim do período.");
        }
        if (inicio.isAfter(fim)) {
            throw new RegraNegocioException("A data de início não pode ser posterior à data de fim.");
        }
        return relatorioRepository.findByDataGeracaoBetween(inicio, fim);
    }
}
