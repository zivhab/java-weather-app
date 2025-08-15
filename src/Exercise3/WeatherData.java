package Exercise3;

// Represents current weather conditions and Inherits common weather fields from WeatherInfo
public class WeatherData extends WeatherInfo {

    // Constructor to initialize current weather data.
    public WeatherData(String temperature, String humidity, String precipitation, String wind) {
        super(temperature, humidity, precipitation, wind);
    }

    // Displays the current weather information in a readable format
    @Override
    public String display() {
        return "🌡️ Temperature: " + getTemperature() + "°C\n" +
                "💧 Precipitation: " + getPrecipitation() + " mm\n" +
                "🌬️ Wind Speed: " + getWind() + " km/h\n" +
                "💦 Humidity: " + getHumidity() + "%";

    }
}
