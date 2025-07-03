package changes.sealedClasses.models;

public final class Quadrado extends Forma {

    private double lado;

    public Quadrado(String nome){
        super(nome);
    }

    public void setLado(double lado) {
        this.lado = lado;
    }

    public double getLado() {
        return lado;
    }
}
