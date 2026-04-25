package br.com.inventario;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal da aplicação de Gestão de Inventário.
 * Exibe um menu interativo em loop até o usuário escolher sair.
 */
public class Principal {

    // Scanner compartilhado por todos os métodos de leitura
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProdutoDAO dao   = new ProdutoDAO();

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Sistema de Gestão de Inventário    ║");
        System.out.println("╚══════════════════════════════════════╝");

        int opcao = -1;

        while (opcao != 5) {

            exibirMenu();

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // limpa o buffer após nextInt()
            } catch (InputMismatchException e) {
                System.out.println("\n❌  Opção inválida. Digite apenas números.");
                scanner.nextLine(); // descarta a entrada inválida
                continue;
            }

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> atualizarProduto();
                case 4 -> excluirProduto();
                case 5 -> System.out.println("\n👋  Encerrando o sistema. Até logo!");
                default -> System.out.println("\n⚠️   Opção inválida. Escolha entre 1 e 5.");
            }
        }

        scanner.close();
    }

    // ================================================================
    // MENU
    // ================================================================

    private static void exibirMenu() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│              MENU PRINCIPAL           │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.println("│  1. Cadastrar Produto                 │");
        System.out.println("│  2. Listar Produtos                   │");
        System.out.println("│  3. Atualizar Produto (por ID)        │");
        System.out.println("│  4. Excluir Produto  (por ID)        │");
        System.out.println("│  5. Sair                              │");
        System.out.println("└──────────────────────────────────────┘");
        System.out.print("  Escolha uma opção: ");
    }

    // ================================================================
    // 1 – CADASTRAR
    // ================================================================

    private static void cadastrarProduto() {
        System.out.println("\n--- Cadastrar Novo Produto ---");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("⚠️   O nome não pode ser vazio.");
            return;
        }

        BigDecimal preco = lerPreco();
        if (preco == null) return; // validação falhou

        int quantidade = lerQuantidade();
        if (quantidade < 0) return; // validação falhou

        Produto produto = new Produto(nome, preco, quantidade);
        dao.salvar(produto);
    }

    // ================================================================
    // 2 – LISTAR
    // ================================================================

    private static void listarProdutos() {
        System.out.println("\n--- Lista de Produtos ---");

        List<Produto> produtos = dao.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("⚠️   Nenhum produto cadastrado ainda.");
            return;
        }

        String cabecalho = String.format(
            "+-%-4s-+-%-30s-+-%-12s-+-%-16s-+",
            "----", "------------------------------",
            "------------", "----------------"
        );

        System.out.println(cabecalho);
        System.out.printf("| %-4s | %-30s | %-12s | %-16s |%n",
                "ID", "Nome", "Preço (R$)", "Quantidade");
        System.out.println(cabecalho);

        for (Produto p : produtos) {
            System.out.println(p);
        }

        System.out.println(cabecalho);
        System.out.println("  Total de produtos: " + produtos.size());
    }

    // ================================================================
    // 3 – ATUALIZAR
    // ================================================================

    private static void atualizarProduto() {
        System.out.println("\n--- Atualizar Produto ---");

        int id = lerId("atualizar");
        if (id < 0) return;

        // Verifica se o produto existe antes de pedir os novos dados
        Produto existente = dao.buscarPorId(id);
        if (existente == null) {
            System.out.println("⚠️   Nenhum produto encontrado com o ID: " + id);
            return;
        }

        System.out.println("Produto atual: " + existente);
        System.out.println("\nDigite os novos dados (Enter = mantém o valor atual):");

        // Nome
        System.out.print("Novo nome [" + existente.getNome() + "]: ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) {
            existente.setNome(novoNome);
        }

        // Preço
        System.out.print("Novo preço [" + existente.getPreco() + "] (ou Enter para manter): ");
        String precoStr = scanner.nextLine().trim();
        if (!precoStr.isEmpty()) {
            try {
                BigDecimal novoPreco = new BigDecimal(precoStr.replace(",", "."));
                if (novoPreco.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("❌  O preço não pode ser negativo. Operação cancelada.");
                    return;
                }
                existente.setPreco(novoPreco);
            } catch (NumberFormatException e) {
                System.out.println("❌  Valor inválido para preço. Operação cancelada.");
                return;
            }
        }

        // Quantidade
        System.out.print("Nova quantidade [" + existente.getQuantidade() + "] (ou Enter para manter): ");
        String qtdStr = scanner.nextLine().trim();
        if (!qtdStr.isEmpty()) {
            try {
                int novaQtd = Integer.parseInt(qtdStr);
                if (novaQtd < 0) {
                    System.out.println("❌  A quantidade não pode ser negativa. Operação cancelada.");
                    return;
                }
                existente.setQuantidade(novaQtd);
            } catch (NumberFormatException e) {
                System.out.println("❌  Valor inválido para quantidade. Operação cancelada.");
                return;
            }
        }

        dao.atualizar(existente);
    }

    // ================================================================
    // 4 – EXCLUIR
    // ================================================================

    private static void excluirProduto() {
        System.out.println("\n--- Excluir Produto ---");

        int id = lerId("excluir");
        if (id < 0) return;

        // Confirma com o usuário antes de excluir
        Produto existente = dao.buscarPorId(id);
        if (existente == null) {
            System.out.println("⚠️   Nenhum produto encontrado com o ID: " + id);
            return;
        }

        System.out.println("Produto encontrado: " + existente);
        System.out.print("Confirma a exclusão? (S/N): ");
        String confirmacao = scanner.nextLine().trim().toUpperCase();

        if ("S".equals(confirmacao)) {
            dao.excluir(id);
        } else {
            System.out.println("⚠️   Exclusão cancelada pelo usuário.");
        }
    }

    // ================================================================
    // HELPERS DE LEITURA VALIDADA
    // ================================================================

    /**
     * Lê e valida um preço: deve ser um número positivo.
     *
     * @return {@link BigDecimal} válido, ou {@code null} em caso de erro
     */
    private static BigDecimal lerPreco() {
        while (true) {
            System.out.print("Preço (R$): ");
            String entrada = scanner.nextLine().trim().replace(",", ".");
            try {
                BigDecimal preco = new BigDecimal(entrada);
                if (preco.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("❌  O preço não pode ser negativo. Tente novamente.");
                    continue;
                }
                return preco;
            } catch (NumberFormatException e) {
                System.out.println("❌  Valor inválido para preço. Use o formato: 19.90");
            }
        }
    }

    /**
     * Lê e valida uma quantidade: deve ser um inteiro não-negativo.
     *
     * @return quantidade válida, ou {@code -1} em caso de erro irrecuperável
     */
    private static int lerQuantidade() {
        while (true) {
            System.out.print("Quantidade em estoque: ");
            String entrada = scanner.nextLine().trim();
            try {
                int qtd = Integer.parseInt(entrada);
                if (qtd < 0) {
                    System.out.println("❌  A quantidade não pode ser negativa. Tente novamente.");
                    continue;
                }
                return qtd;
            } catch (NumberFormatException e) {
                System.out.println("❌  Valor inválido. Digite um número inteiro.");
            }
        }
    }

    /**
     * Lê e valida um ID (inteiro positivo).
     *
     * @param operacao nome da operação para mensagem ao usuário (ex: "excluir")
     * @return ID válido, ou {@code -1} em caso de erro
     */
    private static int lerId(String operacao) {
        System.out.print("Digite o ID do produto que deseja " + operacao + ": ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (id <= 0) {
                System.out.println("❌  O ID deve ser um número positivo.");
                return -1;
            }
            return id;
        } catch (NumberFormatException e) {
            System.out.println("❌  ID inválido. Digite apenas números inteiros.");
            return -1;
        }
    }
}
