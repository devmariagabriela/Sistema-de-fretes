# SISTEMA DE GESTÃO DE FRETES — CONTEXTO DO PROJETO

## Visão Geral

Este projeto consiste em um Sistema de Gestão de Fretes inspirado em sistemas reais de TMS (Transportation Management System), utilizados por transportadoras e operadores logísticos para controle operacional de fretes, veículos, motoristas e entregas.

O sistema foi pensado para representar um ambiente corporativo real, focando não apenas em CRUDs, mas também em:

- fluxo operacional
- rastreamento de entregas
- controle de status
- gestão de frota
- ocorrências logísticas
- auditoria operacional
- relatórios gerenciais
- regras de negócio
- experiência operacional do usuário

---

# Objetivo do Sistema

O objetivo principal do sistema é permitir o gerenciamento completo do ciclo de vida de um frete:

```text
Emissão do frete
→ saída do veículo
→ acompanhamento em rota
→ registro de ocorrências
→ confirmação de entrega
→ geração de relatórios



Conceito do Sistema

O projeto segue o conceito de um TMS corporativo moderno.

O foco da interface e da arquitetura é transmitir:

confiabilidade
controle operacional
produtividade
rastreabilidade
clareza visual
organização logística

O sistema NÃO foi pensado como um site visual/marketing, mas sim como uma ferramenta operacional empresarial.

Arquitetura

O projeto segue obrigatoriamente o padrão em camadas:

JSP → Controller → BO → DAO → PostgreSQL
Responsabilidades
JSP

Responsável apenas pela apresentação visual:

renderização de dados
formulários
mensagens
tabelas
componentes visuais

A JSP não deve possuir:

SQL
regra de negócio
cálculos
lógica operacional
Controller (Servlet)

Responsável por:

receber requisições
validar parâmetros básicos
chamar BO
redirecionar telas

Não deve conter:

SQL
regras de negócio
BO (Business Object)

Camada mais importante do sistema.

Responsável por:

regras de negócio
validações
fluxo operacional
controle de status
transações JDBC
lançamento de exceções

Toda regra obrigatoriamente deve estar aqui.

DAO

Responsável exclusivamente por:

queries SQL
inserts
updates
deletes
consultas
mapeamento ResultSet

Não deve possuir:

regras
validações
lógica operacional
Tecnologias
Backend
Java 8
JSP
Servlets
JDBC
PostgreSQL
Relatórios
JasperReports
Versionamento
Git
GitHub
Conceito Visual

O sistema segue padrões visuais de sistemas corporativos de logística e TMS.

Referências estudadas:

SAP Transportation Management
TOTVS Logística
Senior Sistemas
Intelipost
Fleetio
Project44
FreteBras
Direção de UI/UX

A interface foi projetada priorizando:

produtividade
rapidez operacional
leitura rápida
rastreamento visual
clareza de status
experiência corporativa

O foco da UX é permitir que operadores logísticos consigam executar tarefas rapidamente.

Identidade Visual
Estilo
corporativo
clean
operacional
moderno
logístico
Paleta Principal
Azul escuro (principal)
#0B2344

Representa:

segurança
controle
tecnologia
confiabilidade
Azul destaque
#2D8CFF

Usado em:

botões
ações
links
destaques
Fundo do sistema
#F5F7FA
Cards
#FFFFFF
Texto principal
#1F2937
Bordas
#E5E7EB
Cores Operacionais de Status
ENTREGUE
#16A34A
EM TRÂNSITO
#2563EB
EMITIDO
#D97706
NÃO ENTREGUE
#DC2626
CANCELADO
#6B7280
Estrutura Visual do Sistema
Layout Principal
Sidebar fixa
+
Header superior
+
Dashboard operacional
+
Tabelas
+
Timeline de ocorrências
Sidebar

Módulos principais:

Dashboard
Clientes
Motoristas
Veículos
Fretes
Ocorrências
Relatórios
Manutenções
Dashboard Operacional

Indicadores principais:

Fretes ativos
Em trânsito
Entregues hoje
Atrasados
Veículos disponíveis
Veículos em manutenção
Tabelas

As tabelas são o principal componente operacional do sistema.

Padrões:

hover suave
paginação
filtros
badges de status
visual limpo
espaçamento adequado
leitura rápida
Timeline de Ocorrências

O sistema utiliza timeline operacional para exibir o histórico do frete.

Exemplo:

08:00 - Emitido
10:00 - Saída confirmada
14:00 - Em rota
18:30 - Tentativa de entrega
Regras de Negócio
Veículos
veículo deve estar DISPONÍVEL para novo frete
veículo EM MANUTENÇÃO não pode ser utilizado
veículo EM TRÂNSITO não pode voltar manualmente para DISPONÍVEL
Motoristas
motorista deve estar ATIVO
CNH deve estar válida
motorista não pode estar em viagem simultânea
Fretes
status segue fluxo sequencial
status não pode retroceder
frete cancelado não recebe ocorrência
número do frete gerado no BO

Formato:

FRT-AAAA-NNNNN
Fluxo de Status
EMITIDO
→ SAÍDA_CONFIRMADA
→ EM_TRÂNSITO
→ ENTREGUE

ou:

EM_TRÂNSITO
→ NÃO_ENTREGUE

ou:

EMITIDO
→ CANCELADO
Ocorrências

Tipos:

saída do pátio
em rota
tentativa de entrega
entrega realizada
avaria
extravio
outros
Transações JDBC

Operações compostas utilizam transações JDBC manuais:

conn.setAutoCommit(false);
conn.commit();
conn.rollback();

Objetivo:
garantir consistência operacional.

Enums

Todos os status e tipos utilizam enums Java.

Exemplos:

StatusFrete
StatusVeiculo
TipoOcorrencia
CategoriaCNH

O sistema evita Strings soltas no código.

Tratamento de Erros

O sistema utiliza:

NegocioException
FreteException
CadastroException

Stack trace nunca deve ser exibido ao usuário.

Diferencial Implementado
Controle de manutenção de veículos

Funcionalidades:

manutenção preventiva
manutenção corretiva
bloqueio operacional
histórico de manutenção
alertas visuais

Objetivo:
simular controle real de frota utilizado em transportadoras.

Experiência Profissional

O sistema foi desenvolvido buscando simular:

ERP logístico
TMS corporativo
plataforma operacional real

O objetivo principal foi construir um sistema que parecesse utilizável por uma transportadora real e não apenas um CRUD acadêmico.