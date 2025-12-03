# Visao de POO sobre Usuario

## Modelo: encapsulamento e construtor

Arquivo: `src/main/java/br/com/sportcourt/model/Usuario.java`

```java
public class Usuario {
    private int id;
    private String username;
    private String senha;
    private String email;
    private String role;
    private boolean ativo;
    private String nome;

    public Usuario(int id, String username, String senha, String email,
                   String role, boolean ativo, String nome) {
        this.id = id;
        this.username = username;
        this.senha = senha;
        this.email = email;
        this.role = role;
        this.ativo = ativo;
        this.nome = nome;
    }
    // getters/setters...
}
```

- `Usuario` representa a entidade do dominio e esconde detalhes internos com atributos privados (`encapsulamento`), expondo apenas getters/setters controlados.
- O construtor obriga a criacao de objetos completos, garantindo invariantes basicas.
- O metodo `getSenhaHash` abstrai como a senha e armazenada, liberando a camada externa de conhecer a coluna real do banco.

## DAO: abstracao de persistencia

Arquivo: `src/main/java/br/com/sportcourt/dao/UsuarioDAO.java`

```java
public class UsuarioDAO {
    public Usuario findByUsername(String username) { ... }
    public boolean inserir(Usuario u) { ... }
    public List<Usuario> findAll() { ... }
    public boolean atualizarRole(int id, String role) { ... }
    public boolean resetarSenha(int id, String novaSenha) { ... }
}
```

- A classe aplica o padrao Data Access Object para isolar SQL e conexao de banco (`abstracao` e `separacao de responsabilidades`).
- Usa `Usuario` como tipo de retorno/entrada, mantendo `coesao` entre o modelo de dominio e a camada de persistencia.
- O encapsulamento protege a aplicacao de mudancas futuras no schema, centralizando o acesso aos dados.

## Servico: regra de negocio e delegacao

Arquivo: `src/main/java/br/com/sportcourt/service/UsuarioService.java`

```java
public class UsuarioService {
    private final UsuarioDAO dao = new UsuarioDAO();

    public boolean atualizarRole(Usuario u, String novaRole) {
        boolean ok = dao.atualizarRole(u.getId(), novaRole);
        if (ok) {
            u.setRole(novaRole);
        }
        return ok;
    }
}
```

- `UsuarioService` encapsula regras de negocio (ex.: sincronizar objeto em memoria apos atualizar no banco) e delega persistencia ao DAO (`abstracao` de camadas).
- `composicao` e usada ao manter um `UsuarioDAO` como dependencia interna, reforcando baixo acoplamento.

## Sessao: controle centralizado via membro estatico

Arquivo: `src/main/java/br/com/sportcourt/service/Session.java`

```java
public class Session {
    private static Usuario usuarioLogado;
    public static void setUsuarioLogado(Usuario u) { usuarioLogado = u; }
    public static Usuario getUsuarioLogado() { return usuarioLogado; }
    public static void logout() { usuarioLogado = null; }
}
```

- Usa membro `static` para manter estado compartilhado da aplicacao (`singleton` simples), expondo metodos controlados para acesso/modificacao.
- Encapsula a nocao de usuario autenticado, evitando que controladores armazenem referencias soltas.

## Controllers: heranca e polimorfismo limitado

Arquivo: `src/main/java/br/com/sportcourt/controller/ConfiguracoesController.java`

```java
public class ConfiguracoesController extends BaseController {
    private final UsuarioService service = new UsuarioService();

    @FXML
    private void onAlterarRole() { ... }
}
```

- `ConfiguracoesController` herda de `BaseController`, reutilizando comportamento comun (alertas, confirmacoes), exemplo de `heranca` para reutilizacao de codigo.
- O controlador trabalha com `Usuario` atraves de seus getters (`encapsulamento`) e delega alteracoes ao `UsuarioService`, mantendo `abstracao` da regra de negocio.
- A dependencia e injetada por composicao (`service` como campo), seguindo `acoplamento baixo`: o controller nao conhece detalhes do DAO.

## Autenticacao: polimorfismo por composicao

Arquivo: `src/main/java/br/com/sportcourt/service/AuthService.java`

```java
public class AuthService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String nome, String senhaDigitada) {
        Usuario u = usuarioDAO.findByUsername(nome);
        if (u == null) return null;
        boolean senhaOk = senhaDigitada.equals(u.getSenhaHash());
        return senhaOk ? u : null;
    }
}
```

- `AuthService` abstrai o processo de autenticacao, combinando `encapsulamento` (senha obtida via metodo) e `delegacao` de persistencia.
- O uso de `Usuario` permite substituir implementacoes (ex.: DAO mockado) sem mudar a interface publica do servico (`polimorfismo por substituicao` nas dependencias).

