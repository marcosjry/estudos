package br.com.loja.service;

import br.com.loja.core.Pedido;
import br.com.loja.core.Produto;

public class PedidoService {


    public double calcularTotal(Pedido pedido) {
        if (pedido == null || pedido.getProdutos() == null) {
            return 0.0;
        }

        return pedido.getProdutos().stream()
                .mapToDouble(Produto::preco)
                .sum();
    }
}
