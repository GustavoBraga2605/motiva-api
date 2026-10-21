package br.com.fiap.motiva.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue(RocadaMecanizada.TIPO)
public class RocadaMecanizada extends IntervencaoOperacional {

    public static final String TIPO = "ROCADA_MECANIZADA";

    public RocadaMecanizada() {
    }

    public RocadaMecanizada(Integer trechoId, LocalDate dataExecucao, String observacoes) {
        super(trechoId, dataExecucao, observacoes);
    }

    @Override
    public String executarServico() {
        return "-- Executando roçada mecanizada... --";
    }

    @Override
    public String getTipoServico() {
        return TIPO;
    }
}
