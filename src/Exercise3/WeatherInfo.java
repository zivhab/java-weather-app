package Exercise3;

// Abstract base class representing common weather information.
public abstract class WeatherInfo {
    private String temperature;
    private String humidity;
    private String precipitation;
    private String wind;

    // Constructor to initialize shared weather properties.
    public WeatherInfo(String temperature, String humidity, String precipitation, String wind) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.wind = wind;
    }

    // Getters for shared weather data
    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public String getWind() {
        return wind;
    }


    // Abstract method to be implemented by subclasses for displaying weather data.
    public abstract String display();
}