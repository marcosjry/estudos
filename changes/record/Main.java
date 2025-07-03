package changes.record;

import changes.record.models.Produto;
import changes.record.models.ProdutoDTO;

public class Main {

    public static void main(String[] args) {
        var produto = new ProdutoDTO("ProdutoDTO teste", 200.0, "descrição teste");

        System.out.println("\n"+produto);
        
        Produto novoProduto = new Produto("Produto", "teste", 250.0);
        
        System.out.println("\n"+novoProduto);

        // A verbosidade na classe Produto foi muito maior e o trabalho pra criar consequentemente também.
        // O Record ProdutoDTO foi muito mais direito, fácil de fazer e de entender. Além de possuir todos os métodos
        // que a classe Produto possui.
    }
}
