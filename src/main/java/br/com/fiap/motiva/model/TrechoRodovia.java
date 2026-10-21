package br.com.fiap.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TRECHO_RODOVIA")
public class TrechoRodovia implements MonitoravelViaIoT {

    // A tabela usa GENERATED ALWAYS AS IDENTITY (não há sequences), então a estratégia é IDENTITY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "KM_INICIAL", nullable = false)
    private Integer quilometroInicial;

    @Column(name = "KM_FINAL", nullable = false)
    private Integer quilometroFinal;

    @Column(name = "NIVEL_VEGETACAO", nullable = false)
    private Double nivelVegetacao;

    protected TrechoRodovia() {
        // exigido pelo JPA / Jackson
    }

    public TrechoRodovia(Integer quilometroInicial, Integer quilometroFinal, Double nivelVegetacao) {
        this.quilometroInicial = quilometroInicial;
        this.quilometroFinal = quilometroFinal;
        this.nivelVegetacao = nivelVegetacao;
    }

    public void registrarCrescimento(double taxa) {
        if (taxa <= 0) {
            throw new IllegalArgumentException("Taxa inválida, crescimento não registrado!");
        }
        this.nivelVegetacao += taxa;
    }

    public boolean verificarTrechoCritico() {
        return nivelVegetacao >= 30;
    }

    @Override
    public String transmitirDadosSensor() {
        return "Trecho KM " + quilometroInicial + " ao " + quilometroFinal
                + " --> Vegetação: " + nivelVegetacao + " cm";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getQuilometroInicial() { return quilometroInicial; }
    public void setQuilometroInicial(Integer quilometroInicial) { this.quilometroInicial = quilometroInicial; }

    public Integer getQuilometroFinal() { return quilometroFinal; }
    public void setQuilometroFinal(Integer quilometroFinal) { this.quilometroFinal = quilometroFinal; }

    public Double getNivelVegetacao() { return nivelVegetacao; }
    public void setNivelVegetacao(Double nivelVegetacao) { this.nivelVegetacao = nivelVegetacao; }
}
