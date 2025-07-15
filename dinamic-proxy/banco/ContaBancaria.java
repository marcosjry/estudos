package banco;

public class ContaBancaria implements Conta{
    private double saldo;

    public ContaBancaria(double saldoInicial) { this.saldo = saldoInicial; }

    @Override
    public void depositar(double valor) {
        this.saldo += valor;
    }

    @Override
    public void sacar(double valor) {
        this.saldo -= valor;
    }

    @Override
    public double getSaldo() {
        return this.saldo;
    }
}
