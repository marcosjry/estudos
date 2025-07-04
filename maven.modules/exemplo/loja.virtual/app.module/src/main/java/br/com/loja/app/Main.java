package br.com.loja.app;

import br.com.loja.core.Pedido;
import br.com.loja.core.Produto;
import br.com.loja.service.PedidoService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- Bem-vindo à Loja Virtual ---");

        Produto produto1 = new Produto("Notebook Gamer", 5500.00);
        Produto produto2 = new Produto("Mouse Vertical", 150.50);
        Pedido meuPedido = new Pedido(List.of(produto1, produto2));


        PedidoService pedidoService = new PedidoService();
        double total = pedidoService.calcularTotal(meuPedido);

        // 3. Exibindo o resultado
        System.out.printf("O valor total do pedido é: R$ %.2f\n", total);
        System.out.println("--------------------------------");

        // Exemplo criado usando instâncias das classes do 'core-module'
        // Service presente no 'service-module'
        // E o resultado exibido no console
    }
}
