package br.com.inventario;

import java.math.BigDecimal;

public class Produto {

    private int id;
    private String nome;
    private BigDecimal preco;
    private int quantidade;

    public Produto() {}

    public Produto(String nome, BigDecimal preco, int quantidade) {
        this.nome       = nome;
        this.preco      = preco;
        this.quantidade = quantidade;
    }

    public Produto(int id, String nome, BigDecimal preco, int quantidade) {
        this.id         = id;
        this.nome       = nome;
        this.preco      = preco;
        this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    @Override
    public String toString() {
        return String.format(
            "| %-4d | %-30s | R$ %10.2f | %10d unid. |",
            id, nome, preco, quantidade
        );
    }
}
