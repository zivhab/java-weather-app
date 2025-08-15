package Exercise3;

// class to determine which weather icon to display based on weather conditions.
public class WeatherIconHelper {

    // Returns the appropriate icon path based on weather data
    public static String getIcon(WeatherInfo data) {
        String icon = "sun.png"; // default

        double temp = parseDouble(data.getTemperature());
        double rain = parseDouble(data.getPrecipitation());
        double wind = parseDouble(data.getWind());
        double humidity = parseDouble(data.getHumidity());

        // Wind-based icon selection
        if (wind > 60) {
            icon = "windy.png"; // Very windy conditions
        }

        // Precipitation-based icon selection
        if (rain > 10) {
            icon = "drop.png"; // Heavy rain
        } else if (rain > 0 && rain <= 10) {
            icon = "light-rain.png"; // Light rain or drizzle
        }


        // Cloudiness and humidity-based icon selection
        if (humidity > 80) {
            icon = "cloudy-day.png"; // Overcast or cloudy conditions
        }

        // Temperature-based icon selection
        if (temp <= 0) {
            icon = "snowflake.png"; // Snow or freezing weather
        } else if (temp > 0 && temp <= 10) {
            icon = "cloudy-day.png"; // Cold and likely cloudy
        } else if (temp > 10 && temp <= 20) {
            icon = "sun.png"; // Mild and pleasant, sunny
        } else if (temp > 20 && temp <= 30) {
            icon = "sun.png"; // Warm and sunny
        } else {
            icon = "sun.png"; // Hot, still sunny
        }


        return "/assets/" + icon;
    }

    // Parses a string to extract the numeric value
    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}

