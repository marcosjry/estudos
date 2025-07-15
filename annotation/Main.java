import model.Produto;
import util.JsonSerializer;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Produto produto = new Produto("Notebook Gamer", 7500.50, 10);

        JsonSerializer serializer = new JsonSerializer();
        String json = serializer. serialize(produto);

        System.out.println("Objeto Serializado:");
        System.out.println(json);

    }
}
