package clima;

public class RealWeatherForecast implements WeatherForecast {

    @Override
    public String getForecast(String city) {
        try {
            System.out.println("... Consultando API externa de clima para a cidade de " + city + " ...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if ("Araguari".equalsIgnoreCase(city)) {
            return "Sol com algumas nuvens, 28°C";
        }
        return "Chuva moderada, 22°C";
    }

}
