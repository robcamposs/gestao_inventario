package br.com.inventario;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProdutoDAO dao  = new ProdutoDAO();

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Sistema de Gestão de Inventário    ║");
        System.out.println("╚══════════════════════════════════════╝");

        int opcao = -1;

        while (opcao != 5) {

            exibirMenu();

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("\n❌  Opção inválida. Digite apenas números.");
                scanner.nextLine();
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

    private static void exibirMenu() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│              MENU PRINCIPAL           │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.println("│  1. Cadastrar Produto                 │");
        System.out.println("│  2. Listar Produtos                   │");
        System.out.println("│  3. Atualizar Produto (por ID)        │");
        System.out.println("│  4. Excluir Produto  (por ID)         │");
        System.out.println("│  5. Sair                              │");
        System.out.println("└──────────────────────────────────────┘");
        System.out.print("  Escolha uma opção: ");
    }

    private static void cadastrarProduto() {
        System.out.println("\n--- Cadastrar Novo Produto ---");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("⚠️   O nome não pode ser vazio.");
            return;
        }

        BigDecimal preco = lerPreco();
        if (preco == null) return;

        int quantidade = lerQuantidade();
        if (quantidade < 0) return;

        dao.salvar(new Produto(nome, preco, quantidade));
    }

    private static void listarProdutos() {
        System.out.println("\n--- Lista de Produtos ---");

        List<Produto> produtos = dao.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("⚠️   Nenhum produto cadastrado ainda.");
            return;
        }

        String linha = "+------+--------------------------------+--------------+--------------------+";
        System.out.println(linha);
        System.out.printf("| %-4s | %-30s | %-12s | %-18s |%n", "ID", "Nome", "Preço (R$)", "Quantidade");
        System.out.println(linha);
        for (Produto p : produtos) {
            System.out.println(p);
        }
        System.out.println(linha);
        System.out.println("  Total: " + produtos.size() + " produto(s)");
    }

    private static void atualizarProduto() {
        System.out.println("\n--- Atualizar Produto ---");

        int id = lerId("atualizar");
        if (id < 0) return;

        Produto existente = dao.buscarPorId(id);
        if (existente == null) {
            System.out.println("⚠️   Nenhum produto encontrado com o ID: " + id);
            return;
        }

        System.out.println("Produto atual: " + existente);
        System.out.println("\nDigite os novos dados (Enter = mantém o valor atual):");

        System.out.print("Novo nome [" + existente.getNome() + "]: ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) existente.setNome(novoNome);

        System.out.print("Novo preço [" + existente.getPreco() + "]: ");
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
                System.out.println("❌  Valor inválido. Operação cancelada.");
                return;
            }
        }

        System.out.print("Nova quantidade [" + existente.getQuantidade() + "]: ");
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
                System.out.println("❌  Valor inválido. Operação cancelada.");
                return;
            }
        }

        dao.atualizar(existente);
    }

    private static void excluirProduto() {
        System.out.println("\n--- Excluir Produto ---");

        int id = lerId("excluir");
        if (id < 0) return;

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
            System.out.println("⚠️   Exclusão cancelada.");
        }
    }

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
                System.out.println("❌  Valor inválido. Use o formato: 19.90");
            }
        }
    }

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
