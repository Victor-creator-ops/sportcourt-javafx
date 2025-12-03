# Uso do MySQL no projeto

## Configuracao central

Arquivo: `src/main/java/br/com/sportcourt/config/DatabaseConfig.java`

```java
public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/sportcourt_db";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

- Conexao via JDBC puro; a URL aponta para o schema `sportcourt_db` no host local porta 3306.
- Usuario e senha estao hardcoded; em producao idealmente viria de variaveis de ambiente ou arquivo externo.
- O metodo estatico `getConnection()` e usado por todas as classes DAO com try-with-resources, garantindo fechamento.

## Esquema do banco

Arquivo: `schema.sql`

- Cria o banco `sportcourt_db` e tabelas principais: `usuarios`, `quadras`, `reservas`, `comandas_bar`, `itens_comanda`, `financeiro`, `materiais_esportivos`, `locacoes_materiais`.
- Inclui indices para desempenho e dados seeds (usuario admin, quadras, produtos, materiais).
- Pode ser executado manualmente no MySQL Workbench ou via `mysql -u root -p < schema.sql`.

## Padrão de acesso (DAO)

Exemplo: `src/main/java/br/com/sportcourt/dao/UsuarioDAO.java`

```java
public class UsuarioDAO {
    public Usuario findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND ativo = 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password_hash"), rs.getString("email"),
                        rs.getString("role"), rs.getBoolean("ativo"), rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

- Cada DAO isola SQL e mapeia linhas para objetos de modelo. `PreparedStatement` evita SQL injection e facilita bind de parametros.
- `try-with-resources` fecha conexao, statement e result set automaticamente.
- Operacoes seguem padrao: abrir conexao com `DatabaseConfig`, preparar SQL, preencher parametros, executar, mapear resultados, retornar modelo ou boolean de sucesso.

## Fluxo de uso

1) Controllers chamam servicos (ex.: `UsuarioService`), que delegam a um DAO.  
2) O DAO chama `DatabaseConfig.getConnection()` e executa SQL.  
3) O modelo (ex.: `Usuario`) trafega entre camada de negocio e UI.  
4) Sessao (`Session`) armazena o usuario logado, mas nao interage diretamente com o banco.

## Cuidados e melhorias recomendadas

- Externalizar URL/usuario/senha em variaveis de ambiente ou arquivo de configuracao.
- Usar pool de conexoes (ex.: HikariCP) para reduzir overhead de criacao de conexoes.
- Padronizar log de erros em vez de `printStackTrace`.
- Validar que o driver MySQL (`mysql-connector-j`) esta presente no classpath (incluso via Maven no `pom.xml`).
