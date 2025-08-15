package Exercise3;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// Handles saving, reading, and clearing the city search history

public class FileHandler {
    private static final String FILE_NAME = "city_history.txt";

    // Appends a city name to the history file
    public static void saveCity(String city) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(city);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving city: " + city);
            e.printStackTrace();
        }
    }

    // Reads the city history file and returns a list of unique cities in the order added
    public static List<String> getCityHistory(int maxHistory) {
        Set<String> cities = new LinkedHashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cities.add(line.trim());
            }
        } catch (FileNotFoundException e) {
            // If the file doesn't exist yet
            System.out.println("City history file not found. Starting fresh.");
        } catch (IOException e) {
            System.err.println("Error reading city history.");
            e.printStackTrace();
        }
        return new ArrayList<>(cities);
    }

    // Clears the content of the city history file
    public static void clearCityHistory() {
        try {
            Files.write(Paths.get(FILE_NAME), new byte[0]);
        } catch (IOException e) {
            System.err.println("Error clearing city history.");
            e.printStackTrace();
        }
    }
}
