# Sprint 3 - Sistema MOTIVA

## Integrantes

- Bruno Otávio da Cruz Carvalho - RM 562354
- Gustavo Cordeiro Braga - RM 562247
- João Vitor Santana Silva Ribeiro - RM 564693
- Letícia Gabrielle Andrade Temóteo - RM 563985
- Stefanny Brum dos Santos - RM 566216

## Sobre o projeto

O MOTIVA é um sistema de apoio à gestão de manutenção de rodovias, desenvolvido
ao longo das Sprints 1, 2 e 3 da disciplina de Object Oriented Programming.

Nesta Sprint 3, evoluímos o sistema para persistir seus dados em um banco Oracle
usando JDBC puro. As classes de domínio criadas nas sprints anteriores
(`TrechoRodovia`, `EquipeManutencao`, `IntervencaoOperacional`) ganharam os
campos necessários para virar entidades persistíveis, e passamos a contar com
uma camada de DAOs para cada uma delas.

## Estrutura do projeto

Os pacotes seguem a convenção de domínio invertido (`br.com.fiap.motiva.*`),
igual ao padrão usado em aula (`br.com.ecommerce.*`):

```
src/br/com/fiap/motiva/
├── db/ConexaoBD.java              # Conexão com o Oracle (Singleton)
├── model/                          # Entidades de domínio
│   ├── TrechoRodovia.java              (record)
│   ├── EquipeManutencao.java           (record)
│   ├── IntervencaoOperacional.java     (classe abstrata)
│   ├── Pulverizacao.java               (extends IntervencaoOperacional)
│   ├── RocadaMecanizada.java           (extends IntervencaoOperacional)
│   ├── MonitoravelViaIoT.java          (interface)
│   └── RelatorioPrioridade.java        (record)
├── dao/                             # Um DAO por entidade
│   ├── EquipeManutencaoDAO.java
│   ├── TrechoRodoviaDAO.java
│   ├── IntervencaoOperacionalDAO.java
│   └── RelatorioPrioridadeDAO.java
├── service/GeradorRelatorio.java   # Calcula prioridade e persiste o relatório
└── main/Main.java                  # Demonstra todas as operações (CRUD completo)
sql/
├── seu-script-criacao.sql          # DDL das 4 tabelas
└── seu-script-dados.sql            # Dados de teste
```

## O que o Main demonstra

1. Conexão com o banco Oracle
2. CRUD completo de `EquipeManutencao` (inserir, atualizar, buscar, listar, deletar)
3. Inserção e listagem de `TrechoRodovia`
4. Inserção de `IntervencaoOperacional` - uma `RocadaMecanizada` e uma
   `Pulverizacao`, exercitando o polimorfismo da hierarquia e sua persistência
   via coluna discriminadora (`TIPO_SERVICO`)
5. Geração do relatório de prioridade (impresso no console) com persistência
   automática do resultado
6. Consulta do histórico de relatórios já salvos
7. Remoção (`deletar()`) dos registros de teste inseridos na própria execução
8. Encerramento da conexão


## Decisões de design

- **`TrechoRodovia` e `EquipeManutencao` são `record`**: são entidades simples,
  sem necessidade de herança, então o record elimina boilerplate (equals,
  hashCode, toString e getters "de graça") e representa bem a entidade que
  vai para o banco.
- **`IntervencaoOperacional` continua sendo uma classe abstrata**, não um
  record - records não podem ser estendidos, e a herança/polimorfismo entre
  `Pulverizacao` e `RocadaMecanizada` é o ponto central da modelagem OOP desta
  entidade. A persistência usa uma coluna discriminadora (`TIPO_SERVICO`) para
  reconstruir a subclasse correta ao consultar o banco.
- **`RelatorioPrioridade` é uma entidade nova**: antes o cálculo de prioridade
  só existia no console; agora vira histórico persistido, consultável a
  qualquer momento.
- **Tratamento de exceções**: todo acesso ao banco está isolado em blocos
  `try/catch/finally`, com `PreparedStatement` em todas as queries (nunca
  concatenação de String) para evitar SQL Injection, e fechamento garantido de
  recursos e da conexão.

## Tecnologias utilizadas

- Java 17+ (uso de `record` para as entidades simples)
- JDBC puro (`java.sql.*`), sem frameworks de ORM
- Oracle Database (ambiente FIAP)
- Padrão DAO para separar persistência da lógica de negócio

## Hierarquia de classes

O polimorfismo é o ponto central da modelagem de `IntervencaoOperacional`:

```
IntervencaoOperacional (abstract)
├── Pulverizacao
└── RocadaMecanizada
```

Ambas sobrescrevem `executarServico()` (comportamento) e `getTipoServico()`
(usado como coluna discriminadora para a persistência reconstruir a subclasse
correta ao ler do banco).

## Evolução desde as Sprints 1 e 2

| Nesta Sprint 3 foram acrescentadas: 
| `TrechoRodovia`, `EquipeManutencao` com regras de negócio | Campo `id`; viraram `record` para representar a entidade |
| `IntervencaoOperacional` com apenas `executarServico()` | Campos `trechoId`, `dataExecucao`, `observacoes` - necessários para persistir |
| Cálculo de prioridade dentro do `Main` | Vira `RelatorioPrioridade`, entidade nova com histórico salvo no banco |
| Nenhuma persistência - dados viviam só em memória | `db/`, `dao/` inteiros, novos: conexão Oracle e DAOs com CRUD completo |

## Configuração antes de executar

Por segurança, `src/br/com/fiap/motiva/db/ConexaoBD.java` não contém
credenciais reais — cada integrante deve preencher as próprias antes de
rodar (credenciais de banco nunca devem ser commitadas em um repositório
compartilhado):

```java
private static final String USUARIO = "SEU_RM_AQUI";
private static final String SENHA = "SUA_SENHA_AQUI";
```

Outros pontos de atenção:

- **`ojdbc17.jar`**: precisa estar adicionado como Library/Dependency do
  projeto (`Project Structure > Modules > Dependencies`), não basta estar só
  na pasta `lib/`.
- **Cada RM é um schema Oracle separado**: se você ainda não rodou os scripts
  SQL com o seu próprio usuário, as tabelas não existem no seu schema ainda.
  Rode `sql/seu-script-criacao.sql` e depois `sql/seu-script-dados.sql` antes
  de executar o `Main.java` pela primeira vez.
- **Classe principal**: a classe totalmente qualificada agora é
  `br.com.fiap.motiva.main.Main` (mudou por causa do novo caminho de
  pacotes). Se o IntelliJ reclamar ao rodar, é só criar uma nova configuração
  de Run clicando na seta verde do arquivo `Main.java` de novo — não precisa
  reconfigurar SDK nem Library, só a execução em si.

## Exemplo de execução

```
Conexão com Oracle estabelecida com sucesso!

===== TESTANDO EquipeManutencaoDAO =====

=== INSERIDA: ===

--- EQUIPE DE MANUTENÇÃO ---
Nome da equipe: Equipe Norte
Quantidade de funcionários: 6
Região de atendimento: Região Norte
...

===== TESTANDO IntervencaoOperacionalDAO =====
-- Executando roçada mecanizada... --

-- Executando pulverização... --

 - TODAS AS INTERVENÇÕES: -
ROCADA_MECANIZADA no trecho 3 em 2026-09-04
PULVERIZACAO no trecho 4 em 2026-09-04

===== RELATÓRIO DE PRIORIDADE =====
1.
Trecho KM 15 ao 20 --> Vegetação: 90.0 cm
Prioridade ALTA
-- Intervenção Urgente Necessária --
...

Relatório salvo no banco com ID: 1

===== HISTÓRICO DE RELATÓRIOS =====
Relatório #1 (2026-09-04) — Alta: 2 | Média: 4 | Baixa: 2 — 2 trecho(s) em prioridade ALTA, 4 em MÉDIA e 2 em BAIXA.


===== TESTANDO deletar() =====
Intervenções de teste removidas.
Trechos de teste removidos.
Equipe de teste removida.

=== CONEXÃO ENCERRADA ===
```

## Problemas comuns

| Problema | Causa provável |
|---|---|
| `Driver not found` | `ojdbc17.jar` não está nas dependências do módulo |
| `ORA-01017: invalid username/password` | Usuário/senha incorretos em `ConexaoBD.java` |
| `ORA-00942: table or view does not exist` | Script `seu-script-criacao.sql` não foi executado |
| `ORA-02292: integrity constraint violated` | Tentativa de deletar um `TrechoRodovia` que ainda tem `IntervencaoOperacional` associada |