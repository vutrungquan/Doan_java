package org.example;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class WeatherApp extends JFrame {
    private JTextField textFieldCity;
    private JButton buttonCityWeather;
    private JButton buttonLocationWeather;
    private JLabel labelCity;
    private JLabel labelTemperature;
    private JLabel labelHumidityWind;
    private JLabel labelWeatherIcon;
    private JPanel panelMain;
    private JPanel forecastPanel;
    private final WeatherService weatherService;

    public WeatherApp() {
        weatherService = new WeatherService();
        setupUI();
        setTitle("Weather Forecast App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        buttonCityWeather.addActionListener(e -> displayCurrentWeatherAndForecast(textFieldCity.getText()));
        buttonLocationWeather.addActionListener(e -> displayWeatherByLocation());
    }
    private void displayWeatherByLocation() {
        try {
            double latitude = 21.0285;
            double longitude = 105.8542;

            // Lấy thông tin thời tiết hiện tại theo tọa độ
            CurrentWeather weather = weatherService.getCurrentWeatherByCoordinates(latitude, longitude);
            List<ForecastDay> forecastDays = weatherService.getForecastByCoordinates(latitude, longitude);

            if (weather != null) {
                labelCity.setText("Current Location");
                labelTemperature.setText(String.format("%.2f°C", weather.getTemperature()));
                labelHumidityWind.setText(String.format("Humidity: %.1f%% | Wind: %.1f m/s", weather.getHumidity(), weather.getWindSpeed()));

                updateWeatherIcon(weather.getWeatherCondition());

                // Cập nhật dự báo thời tiết 5 ngày
                updateForecast(forecastDays);

            } else {
                clearWeatherInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
            clearWeatherInfo();
        }
    }
    private void setupUI() {
        // Tạo panel chính với background gradient
        panelMain = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(135, 206, 235),
                        0, getHeight(), new Color(255, 255, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header với title đẹp
        JLabel title = new JLabel("Weather Forecast");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(41, 128, 185));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelMain.add(title);

        // Panel tìm kiếm với style modern
        JPanel searchPanel = createStyledSearchPanel();
        panelMain.add(searchPanel);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel thông tin thời tiết hiện tại
        JPanel currentWeatherPanel = createStyledCurrentWeatherPanel();
        panelMain.add(currentWeatherPanel);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel dự báo thời tiết
        createStyledForecastPanel();
        panelMain.add(forecastPanel);

        // Thêm scrolling cho toàn bộ panel
        JScrollPane scrollPane = new JScrollPane(panelMain);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);
    }

    private JPanel createStyledSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Textfield với style modern
        textFieldCity = new JTextField(20);
        textFieldCity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textFieldCity.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Buttons với style modern
        buttonCityWeather = createStyledButton("Search", "/icons/search_icon.png");
        buttonLocationWeather = createStyledButton("My Location", "/icons/location_icon1.png");

        searchPanel.add(new JLabel("City:"));
        searchPanel.add(textFieldCity);
        searchPanel.add(buttonCityWeather);
        searchPanel.add(buttonLocationWeather);

        return searchPanel;
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Could not load icon: " + iconPath);
        }
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(41, 128, 185));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        return button;
    }

    private JPanel createStyledCurrentWeatherPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Labels với style modern
        labelCity = createStyledLabel("City: ", 18);
        labelTemperature = createStyledLabel("Temperature: ", 24);
        labelHumidityWind = createStyledLabel("Humidity and Wind: ", 16);
        labelWeatherIcon = new JLabel();
        labelWeatherIcon.setHorizontalAlignment(JLabel.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(labelCity);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(labelTemperature);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(labelHumidityWind);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(labelWeatherIcon, BorderLayout.EAST);

        return panel;
    }

    private JLabel createStyledLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    private void createStyledForecastPanel() {
        forecastPanel = new JPanel();
        forecastPanel.setOpaque(false);
        forecastPanel.setLayout(new GridLayout(1, 5, 10, 0));
        forecastPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true),
                "5-Day Forecast",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(41, 128, 185)
        ));
    }

    private void displayCurrentWeatherAndForecast(String cityName) {
        if (cityName.trim().isEmpty()) {
            showErrorMessage("Please enter a city name");
            return;
        }

        try {
            CurrentWeather weather = weatherService.getCurrentWeather(cityName);
            List<ForecastDay> forecastDays = weatherService.getForecast(cityName);
            updateCurrentWeather(cityName, weather);
            updateForecast(forecastDays);
        } catch (SQLException e) {
            showErrorMessage("Error retrieving weather data: " + e.getMessage());
        }
    }

    private void updateCurrentWeather(String cityName, CurrentWeather weather) {
        if (weather != null) {
            labelCity.setText("City: " + cityName);
            labelTemperature.setText(String.format("%.2f°C", weather.getTemperature()));
            labelHumidityWind.setText(String.format("Humidity: %.1f%% | Wind: %.1f m/s", weather.getHumidity(), weather.getWindSpeed()));

            updateWeatherIcon(weather.getWeatherCondition());
        } else {
            clearWeatherInfo();
        }
    }

    private void updateWeatherIcon(String condition) {
        String iconPath = getWeatherIconPath(condition);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            labelWeatherIcon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Could not load weather icon: " + iconPath);
            labelWeatherIcon.setIcon(null);
        }
    }

    private String getWeatherIconPath(String condition) {
        if (condition == null) {
            return "/icons/sunny.png"; // Trả về icon mặc định nếu condition là null
        }

        condition = condition.toLowerCase();
        if (condition.contains("clear")) return "/icons/sunny.png";
        if (condition.contains("cloud")) return "/icons/cloude.png";
        if (condition.contains("rain")) return "/icons/42670-cloud-with-rain-icon.png";
        if (condition.contains("snow")) return "/icons/weather-snow-ice-icon.png";
        if (condition.contains("thunder")) return "/icons/weather-bolt-thunder-icon.png";

        return "/icons/sunny.png"; // Icon mặc định nếu không có điều kiện phù hợp
    }


    private void updateForecast(List<ForecastDay> forecastDays) {
        forecastPanel.removeAll();
        if (forecastDays != null && !forecastDays.isEmpty()) {
            for (ForecastDay day : forecastDays) {
                forecastPanel.add(createForecastDayPanel(day));
            }
        } else {
            forecastPanel.add(createStyledLabel("No forecast data available", 14));
        }
        forecastPanel.revalidate();
        forecastPanel.repaint();
    }

    private JPanel createForecastDayPanel(ForecastDay day) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel dateLabel = createStyledLabel(day.getDate().toLocalDate().toString(), 12);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Weather icon
        JLabel iconLabel = new JLabel();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateForecastIcon(iconLabel, day.getWeatherCondition());

        // Weather info
        String info = String.format("<html><center>%.1f°C / %.1f°C<br>%d%%<br>%.1f m/s</center></html>",
                day.getMinTemperature(), day.getMaxTemperature(),
                day.getHumidity(), day.getWindSpeed());
        JLabel infoLabel = new JLabel(info);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(dateLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(infoLabel);

        return panel;
    }

    private void updateForecastIcon(JLabel label, String condition) {
        String iconPath = getWeatherIconPath(condition);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Could not load forecast icon: " + iconPath);
            label.setIcon(null);
        }
    }

    private void clearWeatherInfo() {
        labelCity.setText("City not found");
        labelTemperature.setText("N/A");
        labelHumidityWind.setText("No data available");
        if (labelWeatherIcon != null) {
            labelWeatherIcon.setIcon(null);
        }
    }
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
    private ImageIcon getWeatherIcon(String weatherCondition) {
        String iconPath = "/icons/";
        switch (weatherCondition.toLowerCase()) {
            case "clear":
                iconPath += "sunny.png";
                break;
            case "clouds":
                iconPath += "cloude.png";
                break;
            case "rain":
                iconPath += "42670-cloud-with-rain-icon.png";
                break;
            case "snow":
                iconPath += "weather-snow-ice-icon.png";
                break;
            default:
                iconPath += "sunny.png";
                break;
        }
        return new ImageIcon(getClass().getResource(iconPath));
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(WeatherApp::new);
    }
}