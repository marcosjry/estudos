package br.com.loja.core;

import java.util.List;

public class Pedido {
    private final List<Produto> produtos;

    public Pedido(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }
}
