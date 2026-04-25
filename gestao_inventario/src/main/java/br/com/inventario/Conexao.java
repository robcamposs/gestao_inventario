package br.com.inventario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados MySQL.
 */
public class Conexao {

    // ---------------------------------------------------------------
    // ATENÇÃO: ajuste as constantes abaixo conforme seu ambiente local
    // ---------------------------------------------------------------
    private static final String URL      = "jdbc:mysql://localhost:3306/aula_jdbc?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO  = "aluno";
    private static final String SENHA    = "senac";

    /**
     * Retorna uma nova conexão com o banco de dados.
     *
     * @return objeto {@link Connection} pronto para uso
     * @throws SQLException caso não seja possível estabelecer a conexão
     */
    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
