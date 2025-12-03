# SportCourt

Aplicação JavaFX para gestão de quadras, reservas e bar (PDV) com foco educacional em POO, modelagem de sistemas e bancos de dados relacionais.

## Objetivos educacionais

- Exercitar POO com camadas Model / DAO / Service / Controller.
- Modelar um domínio real: quadras, reservas, produtos, comandas e financeiro.
- Integrar JavaFX (FXML) com MySQL, persistência e CRUDs completos.
- Praticar regras de negócio e controle de acesso por papéis.

## Principais módulos

- **Autenticação**: login/cadastro, sessão do usuário logado.
- **Quadras**: cadastro e listagem de quadras.
- **Reservas**: agendamento, edição e cancelamento; gera receita.
- **Produtos**: catálogo do bar.
- **Comandas (PDV)**: comandas com itens de produtos, podem ter cliente associado ou não.
- **Financeiro**: lançamentos de entrada/saída e gráfico de fluxo.
- **Configurações**: gestão de usuários e papéis.

## Papéis e permissões

- **ADMIN**: acesso total (financeiro, configurações e todos os módulos).
- **OPERADOR**: pode usar módulos gerais, mas não acessa financeiro.
- **ATENDENTE**: acesso restrito ao PDV (Comandas); demais módulos ocultos.

## Stack

- Java 11 + JavaFX 21
- Maven
- MySQL 8

## Requisitos

- JDK 11+
- Maven 3.8+
- MySQL em execução e acessível

## Configuração do banco

1. Ajuste as credenciais em `src/main/resources/application.properties`.
2. Crie/atualize o schema:
   ```sql
   mysql -u seu_user -p < schema.sql
   ```
   O script cria tabelas e usuário admin padrão (`admin` / `admin123`). Se o banco já existia, execute o `ALTER TABLE` de `schema.sql` para garantir a coluna `cliente_nome` em `comandas_bar`.

## Executando

- Scripts prontos:
  - Linux/macOS: `./run.sh` (tenta instalar Java 11, Maven e cliente MySQL via apt/brew se faltarem, inicia MySQL, aplica `schema.sql` e roda o app)
  - Windows: `run.bat` (tenta instalar via Chocolatey se disponível, inicia MySQL, aplica `schema.sql` e roda o app)
- Via Maven (desenvolvimento):
  ```bash
  mvn -q -DskipTests javafx:run
  ```
- Gerar jar empacotado:
  ```bash
  mvn -q -DskipTests clean package
  java -jar target/sportcourt-system-1.0.0-jar-with-dependencies.jar
  ```

## Estrutura de pastas

- `run.sh`, `run.bat`: scripts para preparar dependências, aplicar o schema e executar.
- `schema.sql`: script de criação/população do banco.
- `src/main/java/br/com/sportcourt/model`: entidades de domínio.
- `src/main/java/br/com/sportcourt/dao`: acesso a dados (JDBC/MySQL).
- `src/main/java/br/com/sportcourt/service`: regras de negócio e integrações (ex.: financeiro).
- `src/main/java/br/com/sportcourt/controller`: controladores JavaFX.
- `src/main/resources/br/com/sportcourt/view`: telas FXML e estilos.
- `src/main/resources/application.properties`: config de banco/tema.

Visão rápida:
```
sportcourt-javafx/
├─ run.sh
├─ run.bat
├─ schema.sql
├─ pom.xml
├─ src/
│  ├─ main/java/br/com/sportcourt/
│  │  ├─ controller/
│  │  ├─ dao/
│  │  ├─ model/
│  │  ├─ service/
│  │  └─ MainApp.java
│  └─ main/resources/br/com/sportcourt/view/
│     ├─ *.fxml
│     └─ styles.css
└─ target/ (gerado pelo build)
```

## Fluxos principais

- **Login**: autentica e direciona conforme o papel (atendente vai direto para PDV).
- **PDV/Comandas**: cria comandas com ou sem cliente; adiciona itens; ao fechar, lança entrada no financeiro.
- **Reservas**: vinculam cliente e quadra; ao marcar pagamento, gera entrada financeira.
- **Financeiro**: permite lançar entradas/saídas manuais e visualizar gráfico de fluxo.
- **Usuários**: ADMIN pode alterar papéis (ADMIN/OPERADOR/ATENDENTE) e resetar senhas.

## Observações

- Projeto pensado para fins acadêmicos: prioriza clareza de camadas e regras de negócio em vez de segurança avançada (hashing forte, migrations automáticas etc.).
- Para ambientes multiusuário ou produção, complemente com migrações, validação robusta e controles de erro/log mais completos.
