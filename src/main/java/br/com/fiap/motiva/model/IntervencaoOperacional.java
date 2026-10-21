package br.com.fiap.motiva.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Hierarquia da Sprint 2 (abstract + polimorfismo) mapeada em UMA tabela.
 * A coluna TIPO_SERVICO, que na Sprint 3 era o "discriminador manual" do DAO,
 * agora é o @DiscriminatorColumn: o Hibernate escolhe a subclasse sozinho.
 */
@Entity
@Table(name = "INTERVENCAO_OPERACIONAL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_SERVICO", discriminatorType = DiscriminatorType.STRING, length = 30)
// No JSON, o campo "tipoServico" diz qual subclasse criar (POST/PUT)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "tipoServico")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Pulverizacao.class, name = Pulverizacao.TIPO),
        @JsonSubTypes.Type(value = RocadaMecanizada.class, name = RocadaMecanizada.TIPO)
})
public abstract class IntervencaoOperacional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FK para TRECHO_RODOVIA (mesmo modelo da Sprint 3); o Service valida se o trecho existe
    @Column(name = "TRECHO_ID", nullable = false)
    private Integer trechoId;

    @Column(name = "DATA_EXECUCAO", nullable = false)
    private LocalDate dataExecucao;

    @Column(name = "OBSERVACOES", length = 255)
    private String observacoes;

    protected IntervencaoOperacional() {
    }

    protected IntervencaoOperacional(Integer trechoId, LocalDate dataExecucao, String observacoes) {
        this.trechoId = trechoId;
        this.dataExecucao = dataExecucao;
        this.observacoes = observacoes;
    }

    @PrePersist
    void definirDataPadrao() {
        // equivalente ao DEFAULT SYSDATE da tabela
        if (dataExecucao == null) {
            dataExecucao = LocalDate.now();
        }
    }

    // Sprint 4: devolve texto em vez de System.out.println
    public abstract String executarServico();

    public abstract String getTipoServico();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTrechoId() { return trechoId; }
    public void setTrechoId(Integer trechoId) { this.trechoId = trechoId; }

    public LocalDate getDataExecucao() { return dataExecucao; }
    public void setDataExecucao(LocalDate dataExecucao) { this.dataExecucao = dataExecucao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
