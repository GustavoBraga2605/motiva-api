package br.com.fiap.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "EQUIPE_MANUTENCAO")
public class EquipeManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NOME_EQUIPE", nullable = false, length = 100)
    private String nomeEquipe;

    @Column(name = "QUANTIDADE_FUNCIONARIOS", nullable = false)
    private Integer quantidadeFuncionarios;

    @Column(name = "REGIAO_ATENDIMENTO", nullable = false, length = 100)
    private String regiaoAtendimento;

    protected EquipeManutencao() {
    }

    public EquipeManutencao(String nomeEquipe, Integer quantidadeFuncionarios, String regiaoAtendimento) {
        this.nomeEquipe = nomeEquipe;
        this.quantidadeFuncionarios = quantidadeFuncionarios;
        this.regiaoAtendimento = regiaoAtendimento;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomeEquipe() { return nomeEquipe; }
    public void setNomeEquipe(String nomeEquipe) { this.nomeEquipe = nomeEquipe; }

    public Integer getQuantidadeFuncionarios() { return quantidadeFuncionarios; }
    public void setQuantidadeFuncionarios(Integer quantidadeFuncionarios) { this.quantidadeFuncionarios = quantidadeFuncionarios; }

    public String getRegiaoAtendimento() { return regiaoAtendimento; }
    public void setRegiaoAtendimento(String regiaoAtendimento) { this.regiaoAtendimento = regiaoAtendimento; }
}
