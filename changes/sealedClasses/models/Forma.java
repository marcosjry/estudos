package changes.sealedClasses.models;

public abstract sealed class Forma permits Circulo, Quadrado, Triangulo {
    private String nome;

    public Forma(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
