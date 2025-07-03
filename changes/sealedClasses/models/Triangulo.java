package changes.sealedClasses.models;

public non-sealed class Triangulo extends Forma {

    private double base;
    private double altura;

    public Triangulo(String nome){
        super(nome);
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getBase() {
        return base;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getAltura() {
        return altura;
    }
}
