package changes.sealedClasses.models;

public class Teste extends Triangulo {
    public Teste(String nome) {
        super(nome);
    }
} 
// Como a classe Triangulo está usando a cláusula non-sealed ela poderá ser extendida por outras classes e consequentemente
// o que ela herdou da classe Forma, também será herdado.

//public class Teste extends Circulo {
//    
//}

// A classe Teste aqui não pode extender de Circulo porque ele é final.
// também não poderia extender de Forma por não estar na cláusula "permits" da classe.

