package br.com.inventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public boolean salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, produto.getNome());
            ps.setBigDecimal(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidade());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produto.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("\nProduto \"" + produto.getNome() + "\" cadastrado com sucesso! (ID: " + produto.getId() + ")");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao salvar produto: " + e.getMessage());
        }

        return false;
    }

    public List<Produto> listarTodos() {
        String sql = "SELECT id, nome, preco, quantidade FROM produtos ORDER BY id";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                produtos.add(new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getBigDecimal("preco"),
                        rs.getInt("quantidade")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }

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
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }

        return null;
    }

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
                System.out.println("\nProduto (ID: " + produto.getId() + ") atualizado com sucesso!");
                return true;
            } else {
                System.out.println("\nNenhum produto encontrado com o ID: " + produto.getId());
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }

        return false;
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = Conexao.obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("\nProduto (ID: " + id + ") excluido com sucesso!");
                return true;
            } else {
                System.out.println("\nNenhum produto encontrado com o ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
        }

        return false;
    }
}
