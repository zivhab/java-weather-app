package Exercise3;

import java.util.HashMap;
import java.util.Map;

// WeatherController handles data retrieval from OpenMeteoClient.
// It creates and returns WeatherInfo objects (WeatherData and ForecastData).
public class WeatherController {

    // Fetches current weather data for a specific city.
    public static WeatherData getCurrentWeather(String city) {
        try {
            System.out.println("Fetching weather for city: " + city);
            OpenMeteoClient.updateCurrentWeather(city);

            return new WeatherData(
                    OpenMeteoClient.getCurrentTemperature(),
                    OpenMeteoClient.getCurrentHumidity(),
                    OpenMeteoClient.getCurrentPrecipitation(),
                    OpenMeteoClient.getCurrentWind()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Fetches a simplified 5-day weather forecast
    public static Map<String, ForecastData> getFiveDayForecast(String city) throws Exception {
        OpenMeteoClient.updateForecast(city);

        String[] times = OpenMeteoClient.getForecastTime();
        String[] temps = OpenMeteoClient.getForecastTemperature();
        String[] winds = OpenMeteoClient.getForecastWind();
        String[] hums = OpenMeteoClient.getForecastHumidity();
        String[] precips = OpenMeteoClient.getForecastPrecipitation();

        Map<String, ForecastData> forecastMap = new HashMap<>();

        // Use a set to track unique days
        for (int i = 0; i < times.length; i++) {
            String date = times[i].split("T")[0];

            // Only add the forecast if it is a new day
            if (!forecastMap.containsKey(date)) {
                forecastMap.put(date, new ForecastData(times[i], temps[i], precips[i], winds[i], hums[i]));
            }

            // Stop once we have 5 forecasts (one per day)
            if (forecastMap.size() >= 5) {
                break;
            }
        }
        return forecastMap;
    }
}
