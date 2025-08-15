package Exercise3;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * WeatherGUI provides a user interface for displaying current weather
 * and 5-day forecasts for a specified city using Swing components.
 */
public class WeatherGUI extends JFrame {

    private static final int MAX_HISTORY = 5;

    // === UI COMPONENTS ===
    private JTextPane currentWeatherArea;
    private JComboBox<String> cityHistoryDropdown;
    private JTextField cityInput;
    private JLabel weatherIconLabel;
    private JLabel statusLabel;
    private JPanel forecastCardsPanel;


    // ===============================
    // Constructor
    // ===============================
    public WeatherGUI() {
        super("Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set background image
        JLabel backgroundLabel = new JLabel(new ImageIcon(getClass().getResource("/assets/background.png")));
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // Transparent content panel layered over background
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        backgroundLabel.add(contentPanel, BorderLayout.CENTER);

        // Build GUI structure
        setupUI(contentPanel);

        setVisible(true);
    }

    // ===============================
    // Main UI Setup
    // ===============================
    private void setupUI(JPanel contentPanel) {
        Font pixelFont = new Font("Monospaced", Font.PLAIN, 14);

        // === TOP PANEL ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topPanel.setOpaque(false);
        topPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // City Label
        JLabel cityLabel = new JLabel("City:");
        cityLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));

        // City Input
        cityInput = new JTextField(15);
        cityInput.setFont(pixelFont);
        cityInput.setPreferredSize(new Dimension(200, 32));

        // Search Button
        JButton searchButton = new JButton("Search");
        configureButton(searchButton, pixelFont);
        searchButton.setPreferredSize(new Dimension(85, 33));

        // Clear Button
        JButton clearButton = new JButton("Clear");
        configureButton(clearButton, pixelFont);
        clearButton.setPreferredSize(new Dimension(85, 33));

        // City History Dropdown
        cityHistoryDropdown = new JComboBox<>();
        cityHistoryDropdown.setFont(pixelFont);
        cityHistoryDropdown.setPreferredSize(new Dimension(170, 33));
        cityHistoryDropdown.setMaximumRowCount(6);
        cityHistoryDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadCityHistory();

        // Set maximum size for components inside topPanel
        cityHistoryDropdown.setMaximumSize(cityHistoryDropdown.getPreferredSize());
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Add components to top panel
        topPanel.add(cityLabel);
        topPanel.add(cityInput);
        topPanel.add(searchButton);
        topPanel.add(cityHistoryDropdown);
        topPanel.add(clearButton);

        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(topPanel);

        // === STATUS LABEL ===
        statusLabel = new JLabel("Enter a city to get started.");
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 15));
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(10));


        // === CURRENT WEATHER PANEL ===
        JPanel weatherCardPanel = createWeatherCardPanel();
        weatherIconLabel = new JLabel();
        weatherIconLabel.setPreferredSize(new Dimension(150, 150));
        weatherIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        currentWeatherArea = new JTextPane();
        configureWeatherTextPane(currentWeatherArea);

        JScrollPane scrollPane = new JScrollPane(currentWeatherArea);
        configureScrollPane(scrollPane);

        weatherCardPanel.add(weatherIconLabel);
        weatherCardPanel.add(Box.createVerticalStrut(20));
        weatherCardPanel.add(scrollPane);

        JLabel currentWeatherLabel = new JLabel("Current Weather");
        currentWeatherLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        currentWeatherLabel.setForeground(Color.DARK_GRAY);
        currentWeatherLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(currentWeatherLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(weatherCardPanel);

        // === 5-DAY FORECAST PANEL ===
        JLabel forecastTitle = new JLabel("5-Day Forecast");
        forecastTitle.setFont(new Font("Monospaced", Font.BOLD, 18));
        forecastTitle.setForeground(Color.DARK_GRAY);
        forecastTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        forecastCardsPanel = new JPanel();
        forecastCardsPanel.setLayout(new BoxLayout(forecastCardsPanel, BoxLayout.X_AXIS));
        forecastCardsPanel.setOpaque(false);

        JPanel forecastContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        forecastContainer.setOpaque(false);
        forecastContainer.setLayout(new BorderLayout());
        forecastContainer.setPreferredSize(new Dimension(520, 250));
        forecastContainer.setMaximumSize(new Dimension(520, 250));
        forecastContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane forecastScroll = new JScrollPane(forecastCardsPanel);
        forecastScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        forecastScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        forecastScroll.setBorder(null);
        forecastScroll.setOpaque(false);
        forecastScroll.getViewport().setOpaque(false);

        forecastContainer.add(forecastScroll, BorderLayout.CENTER);

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(forecastTitle);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(forecastContainer);

        // === EVENT LISTENERS ===
        searchButton.addActionListener(e -> performSearch());
        cityInput.addActionListener(e -> performSearch());

        clearButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Clear history?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                FileHandler.clearCityHistory();
                updateCityHistory();
            }
        });

        cityHistoryDropdown.addActionListener(e -> {
            String selectedCity = (String) cityHistoryDropdown.getSelectedItem();
            if (selectedCity != null && !selectedCity.equals("Previous Cities")) {
                cityInput.setText(selectedCity);
                performSearch();
            }
        });
    }

    // ===============================
    // Component Configuration Helpers
    // ===============================
    private void configureButton(JButton button, Font font) {
        button.setFont(font);
        button.setPreferredSize(new Dimension(85, 33));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configureWeatherTextPane(JTextPane pane) {
        pane.setFont(new Font("Monospaced", Font.BOLD, 18));
        pane.setEditable(false);
        pane.setForeground(Color.DARK_GRAY);
        pane.setOpaque(false);
        pane.setBackground(new Color(0, 0, 0, 0));

        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    private void configureScrollPane(JScrollPane scroll) {
        scroll.setPreferredSize(new Dimension(350, 100));
        scroll.setMaximumSize(new Dimension(350, 100));
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
    }

    private JPanel createWeatherCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(420, 300));
        panel.setMaximumSize(new Dimension(420, 300));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }

    // ===============================
    // Weather Logic
    // ===============================
    private void performSearch() {
        String city = cityInput.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city.");
            return;
        }

        statusLabel.setText("Fetching weather for " + city + "...");
        SwingUtilities.invokeLater(() -> {
            FileHandler.saveCity(city);
            updateCityHistory();
            displayWeather(city);
        });
    }

    private void displayWeather(String city) {
        try {
            WeatherData current = WeatherController.getCurrentWeather(city);

            if (current == null) {
                currentWeatherArea.setText("Unable to fetch weather data.");
                forecastCardsPanel.removeAll();
                forecastCardsPanel.revalidate();
                forecastCardsPanel.repaint();
                statusLabel.setText("Error: could not retrieve weather.");
                statusLabel.setForeground(Color.RED);
                return;
            }

            currentWeatherArea.setText(current.display());

            StyledDocument doc = currentWeatherArea.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            String iconPath = WeatherIconHelper.getIcon(current);
            var iconURL = getClass().getResource(iconPath);
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                weatherIconLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            } else {
                weatherIconLabel.setIcon(null);
            }

            Map<String, ForecastData> forecastMap = WeatherController.getFiveDayForecast(city);
            forecastCardsPanel.removeAll();

            for (ForecastData data : forecastMap.values()) {
                forecastCardsPanel.add(Box.createHorizontalStrut(10));
                forecastCardsPanel.add(createForecastCard(data));
            }

            forecastCardsPanel.revalidate();
            forecastCardsPanel.repaint();
            statusLabel.setText("Weather for " + city + " loaded!");
            statusLabel.setForeground(Color.BLACK);

        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setForeground(Color.RED);
        }
    }

    private JPanel createForecastCard(ForecastData data) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 3, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(200, 180));
        card.setMaximumSize(new Dimension(200, 180));

        // Add icon
        String iconPath = WeatherIconHelper.getIcon(data);
        var iconURL = getClass().getResource(iconPath);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            JLabel iconLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH)));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(iconLabel);
            card.add(Box.createVerticalStrut(10));
        }

        // Format date
        String[] splitTime = data.getTime().split("T");
        String dateFormatted = formatDate(splitTime[0]) + ", " + splitTime[1];

        // Add forecast labels
        List<JLabel> labels = List.of(
                new JLabel("📅 " + dateFormatted),
                new JLabel("🌡 Temp: " + data.getTemperature() + "°C"),
                new JLabel("💧 Rain: " + data.getPrecipitation() + " mm"),
                new JLabel("🌬 Wind: " + data.getWind() + " km/h"),
                new JLabel("💦 Humidity: " + data.getHumidity() + "%")
        );

        for (JLabel label : labels) {
            label.setFont(new Font("Monospaced", Font.PLAIN, 11));
            label.setForeground(Color.DARK_GRAY);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(label);
            card.add(Box.createVerticalStrut(5));
        }

        return card;
    }

    private String formatDate(String isoDate) {
        try {
            java.time.LocalDate date = java.time.LocalDate.parse(isoDate);
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("E, dd MMMM");
            return date.format(formatter);
        } catch (Exception e) {
            return isoDate;
        }
    }

    // ===============================
    // City History Management
    // ===============================
    private void loadCityHistory() {
        List<String> history = FileHandler.getCityHistory(MAX_HISTORY);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Previous Cities");
        history.forEach(model::addElement);
        cityHistoryDropdown.setModel(model);
    }

    private void updateCityHistory() {
        loadCityHistory();
    }
}