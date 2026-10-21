package br.com.fiap.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "RELATORIO_PRIORIDADE")
public class RelatorioPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "DATA_GERACAO", nullable = false)
    private LocalDate dataGeracao;

    @Column(name = "QTD_ALTA", nullable = false)
    private Integer qtdAlta;

    @Column(name = "QTD_MEDIA", nullable = false)
    private Integer qtdMedia;

    @Column(name = "QTD_BAIXA", nullable = false)
    private Integer qtdBaixa;

    @Column(name = "RESUMO", length = 500)
    private String resumo;

    protected RelatorioPrioridade() {
    }

    public RelatorioPrioridade(LocalDate dataGeracao, Integer qtdAlta, Integer qtdMedia, Integer qtdBaixa, String resumo) {
        this.dataGeracao = dataGeracao;
        this.qtdAlta = qtdAlta;
        this.qtdMedia = qtdMedia;
        this.qtdBaixa = qtdBaixa;
        this.resumo = resumo;
    }

    @PrePersist
    void definirDataPadrao() {
        if (dataGeracao == null) {
            dataGeracao = LocalDate.now();
        }
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDate dataGeracao) { this.dataGeracao = dataGeracao; }

    public Integer getQtdAlta() { return qtdAlta; }
    public void setQtdAlta(Integer qtdAlta) { this.qtdAlta = qtdAlta; }

    public Integer getQtdMedia() { return qtdMedia; }
    public void setQtdMedia(Integer qtdMedia) { this.qtdMedia = qtdMedia; }

    public Integer getQtdBaixa() { return qtdBaixa; }
    public void setQtdBaixa(Integer qtdBaixa) { this.qtdBaixa = qtdBaixa; }

    public String getResumo() { return resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }
}
