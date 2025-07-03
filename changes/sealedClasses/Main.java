package changes.sealedClasses;

import changes.sealedClasses.models.Teste;

public class Main {
    public static void main(String[] args) {
        
        Teste teste = new Teste("Testanto a herança");

        System.out.println("\n"+teste.getNome());

    }
}
