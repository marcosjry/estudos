package changes.record.models;

public class Produto {
    private String nome;
    private Double valor;
    private String descricao;


    public Produto(String nome, String descricao, Double valor) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return String.format("Produto[nome=%s, valor=%.2f, descricao=%f]", nome, valor, descricao);
    }
}
