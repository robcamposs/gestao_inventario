# Gestão de Inventário — JDBC + MySQL + Maven

Aplicação console Java para gerenciamento de produtos em banco de dados MySQL,
desenvolvida com JDBC e Maven.

---

## Pré-requisitos

| Ferramenta | Versão mínima |
|------------|--------------|
| Java (JDK) | 11           |
| Maven      | 3.6          |
| MySQL      | 5.7 / 8.x    |

---

## Configuração Rápida

### 1. Banco de dados

Execute o script SQL no MySQL Workbench ou via CLI:

```bash
mysql -u root -p < banco_de_dados.sql
```

Isso cria o banco `aula_jdbc` e a tabela `produtos` (com dados de exemplo opcionais).

### 2. Ajuste a senha no código

Abra `src/main/java/br/com/inventario/Conexao.java` e edite a constante `SENHA`:

```java
private static final String SENHA = "SUA_SENHA_AQUI";
```

### 3. Compile e execute

```bash
# Na raiz do projeto (onde está o pom.xml):
mvn clean compile

# Executar diretamente pelo Maven:
mvn exec:java -Dexec.mainClass="br.com.inventario.Principal"

# OU gerar o JAR e rodar:
mvn clean package
java -cp target/gestao-inventario-jdbc-1.0-SNAPSHOT.jar br.com.inventario.Principal
```

---

## Estrutura do Projeto

```
gestao_inventario/
├── pom.xml                          # Dependências Maven (mysql-connector-j)
├── banco_de_dados.sql               # Script de criação do banco
└── src/
    └── main/
        └── java/
            └── br/com/inventario/
                ├── Conexao.java     # Fábrica de Connection JDBC
                ├── Produto.java     # POJO com id, nome, preco, quantidade
                ├── ProdutoDAO.java  # CRUD com PreparedStatement
                └── Principal.java  # main() com menu switch-case
```

---

## Funcionalidades

| Opção | Descrição |
|-------|-----------|
| 1     | Cadastrar produto (com validação de preço negativo) |
| 2     | Listar todos os produtos em tabela formatada |
| 3     | Atualizar produto por ID (campos opcionais) |
| 4     | Excluir produto por ID (com confirmação S/N) |
| 5     | Sair da aplicação |

---

## Critérios de Avaliação (Checklist)

- ✅ **Conexão Segura** — `try-with-resources` fecha `Connection` e `PreparedStatement` automaticamente
- ✅ **PreparedStatement** — todas as queries usam `?` (sem concatenação de strings → sem SQL Injection)
- ✅ **Feedback ao Usuário** — mensagens de sucesso, aviso e erro em cada operação
- ✅ **Tratamento de Exceções** — `SQLException` capturado e exibido de forma amigável
- ✅ **Validação** — preço negativo é rejeitado; confirmação antes de excluir

---

## Exemplo de Execução

```
╔══════════════════════════════════════╗
║   Sistema de Gestão de Inventário    ║
╚══════════════════════════════════════╝

┌──────────────────────────────────────┐
│              MENU PRINCIPAL           │
├──────────────────────────────────────┤
│  1. Cadastrar Produto                 │
│  2. Listar Produtos                   │
│  3. Atualizar Produto (por ID)        │
│  4. Excluir Produto  (por ID)         │
│  5. Sair                              │
└──────────────────────────────────────┘
  Escolha uma opção: 2

--- Lista de Produtos ---
+------+--------------------------------+--------------+------------------+
| ID   | Nome                           | Preço (R$)   | Quantidade       |
+------+--------------------------------+--------------+------------------+
| 1    | Teclado Mecânico               | R$     349,90 |          15 unid.|
| 2    | Mouse Gamer                    | R$     189,99 |          30 unid.|
...
```
