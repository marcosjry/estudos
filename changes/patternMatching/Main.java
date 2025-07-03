package changes.patternMatching;

import changes.patternMatching.model.Ponto;
import changes.sealedClasses.models.Circulo;
import changes.sealedClasses.models.Forma;
import changes.sealedClasses.models.Quadrado;
import changes.sealedClasses.models.Triangulo;

public class Main {
    // Reaproveitando as Sealed Classes
    public static double getArea(Forma forma) {
        return switch(forma) {

            case Circulo c -> Math.PI * c.getRaio() * c.getRaio();

            case Quadrado q -> q.getLado() * q.getLado();

            case Triangulo t -> (t.getBase() * t.getAltura()) / 2;

            default -> 0;
        };
    }

    // Desestruturando as propriedades de um Record com o Pattern Matching
    public static void processaPonto(Object obj) {
        switch(obj) {
            
            case Ponto(int x, int y) -> {
                var conta = x * y;
                System.out.printf("\nPonto na posição (%d, %d)", x, y);
                System.out.printf("\nResultado da multiplicação de %d por %d = %d", x, y, conta);
            }

            default -> System.out.println("\nO objeto não é um Ponto.");
        }
    }

    public static void main(String[] args) {
        
        Quadrado quadrado = new Quadrado("quadrado");

        quadrado.setLado(10);

        var area = getArea(quadrado);
        System.out.println("Área = "+ area);

        Ponto ponto = new Ponto(5, 20);
        processaPonto(ponto);
    }
}
