package br.com.inventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso aos dados (DAO) para a entidade {@link Produto}.
 * Toda comunicação com a tabela {@code produtos} do banco passa por aqui.
 */
public class ProdutoDAO {

    // ================================================================
    // SALVAR (INSERT)
    // ================================================================

    /**
     * Persiste um novo produto na tabela {@code produtos}.
     *
     * @param produto objeto a ser salvo (id será ignorado — gerado pelo banco)
     * @return {@code true} se ao menos uma linha foi inserida, {@code false} caso contrário
     */
    public boolean salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, produto.getNome());
            ps.setBigDecimal(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                // Recupera o ID gerado pelo banco e atualiza o objeto
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produto.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("\n✅  Produto \"" + produto.getNome()
                        + "\" cadastrado com sucesso! (ID: " + produto.getId() + ")");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌  Erro ao salvar produto: " + e.getMessage());
        }

        return false;
    }

    // ================================================================
    // LISTAR TODOS (SELECT)
    // ================================================================

    /**
     * Recupera todos os produtos cadastrados.
     *
     * @return lista de {@link Produto}; vazia se não houver registros
     */
    public List<Produto> listarTodos() {
        String sql = "SELECT id, nome, preco, quantidade FROM produtos ORDER BY id";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getBigDecimal("preco"),
                        rs.getInt("quantidade")
                );
                produtos.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌  Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }

    // ================================================================
    // BUSCAR POR ID (SELECT)
    // ================================================================

    /**
     * Busca um único produto pelo seu ID.
     *
     * @param id identificador do produto
     * @return o {@link Produto} encontrado, ou {@code null} se não existir
     */
    public Produto buscarPorId(int id) {
        String sql = "SELECT id, nome, preco, quantidade FROM produtos WHERE id = ?";

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getBigDecimal("preco"),
                            rs.getInt("quantidade")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("❌  Erro ao buscar produto: " + e.getMessage());
        }

        return null;
    }

    // ================================================================
    // ATUALIZAR (UPDATE)
    // ================================================================

    /**
     * Atualiza os dados de um produto existente com base no seu ID.
     *
     * @param produto objeto com os novos valores (deve ter um id válido)
     * @return {@code true} se o produto foi encontrado e atualizado
     */
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produto.getNome());
            ps.setBigDecimal(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());
            ps.setInt(4, produto.getId());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("\n✅  Produto (ID: " + produto.getId() + ") atualizado com sucesso!");
                return true;
            } else {
                System.out.println("\n⚠️   Nenhum produto encontrado com o ID: " + produto.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌  Erro ao atualizar produto: " + e.getMessage());
        }

        return false;
    }

    // ================================================================
    // EXCLUIR (DELETE)
    // ================================================================

    /**
     * Remove um produto da tabela pelo seu ID.
     *
     * @param id identificador do produto a ser excluído
     * @return {@code true} se o produto foi encontrado e removido
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("\n✅  Produto (ID: " + id + ") excluído com sucesso!");
                return true;
            } else {
                System.out.println("\n⚠️   Nenhum produto encontrado com o ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("❌  Erro ao excluir produto: " + e.getMessage());
        }

        return false;
    }
}
