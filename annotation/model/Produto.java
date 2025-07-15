package model;

import util.JsonField;

public class Produto {

    @JsonField("nome_produto")
    private String nome;

    @JsonField(value = "preco_final", obrigatorio = false)
    private double preco;

    private int estoque;

    public Produto (String nome, double preco, int estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }
}
