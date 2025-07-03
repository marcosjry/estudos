package changes.var;
public class Var {
    public static void main(String[] args) {
        var texto = "texto qualquer";

        System.out.println(texto);
        
        var number = 1;
        System.out.println(number);

        // a inferência de tipo o próprio compilador faz pela atribuição do valor na inicialização da variável
        // no primeiro caso foi String e no segundo um int
        
    }
}
