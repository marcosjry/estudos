package banco;

public interface Conta {
    void depositar(double valor);
    void sacar(double valor);
    double getSaldo();
}
