package br.com.fiap.motiva.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue(Pulverizacao.TIPO)
public class Pulverizacao extends IntervencaoOperacional {

    public static final String TIPO = "PULVERIZACAO";

    public Pulverizacao() {
    }

    public Pulverizacao(Integer trechoId, LocalDate dataExecucao, String observacoes) {
        super(trechoId, dataExecucao, observacoes);
    }

    @Override
    public String executarServico() {
        return "-- Executando pulverização... --";
    }

    @Override
    public String getTipoServico() {
        return TIPO;
    }
}
