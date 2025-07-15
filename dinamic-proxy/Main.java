import banco.Conta;
import banco.ContaBancaria;
import banco.LoggingInvocationHandler;
import clima.CachingInvocationHandler;
import clima.RealWeatherForecast;
import clima.WeatherForecast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {

        Conta contaReal = new ContaBancaria(1000.0);

        InvocationHandler handler = new LoggingInvocationHandler(contaReal);

        Conta contaProxy = (Conta) Proxy.newProxyInstance(
                Conta.class.getClassLoader(),
                new Class<?>[]{Conta.class},
                handler
        );

        System.out.println("Saldo inicial: " + contaProxy.getSaldo());
        System.out.println();
        contaProxy.depositar(500.0);
        System.out.println();
        System.out.println("Novo Saldo: " + contaProxy.getSaldo());


        WeatherForecast realService = new RealWeatherForecast();

        // 2. Cria o proxy dinâmico de cache
        WeatherForecast cachedService = (WeatherForecast) Proxy.newProxyInstance(
                WeatherForecast.class.getClassLoader(),
                new Class<?>[]{ WeatherForecast.class },
                new CachingInvocationHandler(realService)
        );

        System.out.println("--- PRIMEIRA CHAMADA PARA ARAGUARI ---");
        long startTime1 = System.currentTimeMillis();
        String forecast1 = cachedService.getForecast("Araguari");
        long endTime1 = System.currentTimeMillis();
        System.out.println("Previsão: '" + forecast1 + "'");
        System.out.println("Tempo gasto: " + (endTime1 - startTime1) + "ms\n");

        System.out.println("--- SEGUNDA CHAMADA PARA ARAGUARI ---");
        long startTime2 = System.currentTimeMillis();
        String forecast2 = cachedService.getForecast("Araguari");
        long endTime2 = System.currentTimeMillis();
        System.out.println("Previsão: '" + forecast2 + "'");
        System.out.println("Tempo gasto: " + (endTime2 - startTime2) + "ms\n");

        System.out.println("--- PRIMEIRA CHAMADA PARA UBERLÂNDIA ---");
        long startTime3 = System.currentTimeMillis();
        String forecast3 = cachedService.getForecast("Uberlândia");
        long endTime3 = System.currentTimeMillis();
        System.out.println("Previsão: '" + forecast3 + "'");
        System.out.println("Tempo gasto: " + (endTime3 - startTime3) + "ms");
    }
}
