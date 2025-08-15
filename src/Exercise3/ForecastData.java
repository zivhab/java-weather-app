package Exercise3;

// Represents forecasted weather data for a specific time and Inherits common weather fields from WeatherInfo
public class ForecastData extends WeatherInfo {
    private String time;

    // Constructor to initialize forecast data.
    public ForecastData(String time, String temperature, String humidity, String precipitation, String wind) {
        super(temperature, humidity, precipitation, wind);
        this.time = time;
    }

    // Gets the forecast time
    public String getTime() {
        return time;
    }

    // Displays the forecast weather information in a readable format.
    @Override
    public String display() {
        String formattedTime = time.replace("T", ", Time ");
        return "Forecast - Date: " + formattedTime + "\n" +
                "Temperature: " + getTemperature() + "°C\n" +
                "Precipitation: " + getPrecipitation() + " mm\n" +
                "Wind Speed: " + getWind() + " km/h\n" +
                "Humidity: " + getHumidity() + "%";
    }
}