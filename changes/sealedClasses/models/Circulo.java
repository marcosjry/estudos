package changes.sealedClasses.models;

public final class Circulo extends Forma {

    private double raio;

    public Circulo(String nome){
        super(nome);
    }

    public void setRaio(double lado) {
        this.raio = lado;
    }

    public double getRaio() {
        return raio;
    }
}
