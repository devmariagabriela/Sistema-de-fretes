# GW FRETE

Sistema corporativo para gestao de fretes e operacao logistica. O projeto centraliza cadastros, operacoes, financeiro, manutencoes, rastreamentos, notificacoes, dashboards e relatorios em PDF/Excel.

## Principais Recursos

- Autenticacao de usuarios com perfis de acesso.
- Dashboard operacional com indicadores executivos.
- Cadastro e gestao de clientes, veiculos, motoristas e usuarios.
- Controle de fretes, contratos, faturas e manutencoes.
- Registro de ocorrencias e rastreamentos de frete.
- Central de notificacoes operacionais.
- Relatorios gerenciais e operacionais com exportacao em PDF e Excel.
- Recuperacao e redefinicao de senha.

## Tecnologias

- Java 8
- Servlet API 3.1
- JSP + JSTL
- Maven
- PostgreSQL
- BCrypt
- JasperReports
- Apache Tomcat 9
- HTML, CSS e JavaScript

## Estrutura do Projeto

```text
src/main/java/br/com/gwfrete
├── cliente/        # Cliente, BO, DAO e controller
├── contrato/       # Contratos e status contratuais
├── dashboard/      # Indicadores e KPIs
├── exception/      # Excecoes de autenticacao e cadastro
├── filter/         # Filtros da aplicacao
├── financeiro/     # Faturas e financeiro
├── frete/          # Fretes e status operacionais
├── manutencao/     # Manutencoes de veiculos
├── motorista/      # Motoristas, CNH e vinculos
├── notificacao/    # Notificacoes do sistema
├── ocorrencia/     # Ocorrencias de frete
├── rastreamento/   # Rastreamentos de frete
├── relatorio/      # DTOs, consultas e exportacao PDF/Excel
├── usuario/        # Login, usuarios, perfis e recuperacao de senha
├── util/           # Conexao e criptografia
└── veiculo/        # Veiculos, tipos e status
```

```text
src/main/webapp
├── assets/         # CSS e JavaScript
├── WEB-INF/views/  # Telas JSP protegidas
├── login.jsp       # Tela de login
└── index.jsp       # Entrada da aplicacao
```

```text
database/           # Scripts SQL de criacao/atualizacao
scripts/            # Scripts auxiliares para Tomcat
```

## Requisitos

- JDK 8
- Maven
- PostgreSQL
- Tomcat 9

## Banco de Dados

Crie o banco usado pela aplicacao e execute os scripts da pasta `database/` em ordem numerica:

```bash
psql -U postgres -d gw_frete -f database/001_criar_tabela_usuario.sql
psql -U postgres -d gw_frete -f database/002_estrutura_usuarios.sql
psql -U postgres -d gw_frete -f database/003_estrutura_veiculos.sql
psql -U postgres -d gw_frete -f database/004_estrutura_motoristas.sql
psql -U postgres -d gw_frete -f database/005_estrutura_fretes.sql
psql -U postgres -d gw_frete -f database/006_estrutura_ocorrencias_frete.sql
psql -U postgres -d gw_frete -f database/007_estrutura_clientes.sql
psql -U postgres -d gw_frete -f database/008_estrutura_financeiro.sql
psql -U postgres -d gw_frete -f database/009_estrutura_manutencao_veiculos.sql
psql -U postgres -d gw_frete -f database/010_estrutura_notificacoes.sql
psql -U postgres -d gw_frete -f database/011_estrutura_rastreamento_fretes.sql
psql -U postgres -d gw_frete -f database/012_estrutura_contratos.sql
psql -U postgres -d gw_frete -f database/013_view_kpis_executivos.sql
psql -U postgres -d gw_frete -f database/014_estrutura_recuperacao_senha.sql
```

Usuario inicial criado pelos scripts:

```text
Email: admin@gwfrete.com
Senha: admin123
```

## Configuracao

A conexao com o banco pode ser configurada por variaveis de ambiente:

```bash
export GWFRETE_DB_URL="jdbc:postgresql://localhost:55432/gw_frete"
export GWFRETE_DB_USUARIO="postgres"
export GWFRETE_DB_SENHA="postgres"
```

Tambem e possivel usar propriedades Java:

```bash
-Dgwfrete.db.url=jdbc:postgresql://localhost:55432/gw_frete
-Dgwfrete.db.usuario=postgres
-Dgwfrete.db.senha=postgres
```

## Como Rodar

Gerar o pacote WAR:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn clean package -DskipTests
```

Implantar no Tomcat local configurado pelos scripts:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 ./scripts/tomcat-build-deploy.sh
```

Iniciar o Tomcat:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 ./scripts/tomcat-start.sh
```

Parar o Tomcat:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 ./scripts/tomcat-stop.sh
```

URL padrao usada pelos scripts:

```text
http://localhost:18087/gw-frete/login
```

## Relatorios

A central de relatorios fica em:

```text
/relatorios
```

Relatorios disponiveis:

- Fretes
- Contratos
- Financeiro
- Manutencoes
- Ocorrencias
- Clientes
- Motoristas
- Veiculos

Cada relatorio pode ser visualizado na tela e exportado em:

- PDF
- Excel `.xlsx`

## Comandos Uteis

Compilar sem testes:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn -q clean package -DskipTests
```

Ver arquivos modificados:

```bash
git status --short
```

Gerar WAR final:

```bash
mvn package
```

O WAR gerado fica em:

```text
target/gw-frete.war
```

## Observacoes de Desenvolvimento

- As telas protegidas ficam em `WEB-INF/views`.
- As rotas sao configuradas em `src/main/webapp/WEB-INF/web.xml`.
- A autenticacao passa pelo filtro `br.com.gwfrete.filter.AuthFilter`.
- A organizacao principal do backend segue por dominio, reunindo model, BO, DAO e controller na mesma pasta.
- Os relatorios usam JRXML em `src/main/resources/report`.
- Evite guardar sessoes antigas apos reorganizar pacotes; se houver erro de sessao, faca logout ou limpe os cookies do navegador.
