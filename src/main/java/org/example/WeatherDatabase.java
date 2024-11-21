package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherDatabase {

    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/WeatherApp";
        String user = "root";
        String password = "trungquanadg123";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Connection failed! Error: " + e.getMessage());
            return null;
        }
    }

    // Cập nhật dự báo thời tiết 5 ngày vào bảng Forecast
    public void updateForecast(int cityId, List<ForecastDay> forecastDays) throws SQLException {
        String sql = "INSERT INTO Forecast (city_id, date, min_temperature, max_temperature, humidity, wind_speed) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect()) {
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (ForecastDay day : forecastDays) {
                        pstmt.setInt(1, cityId);
                        pstmt.setTimestamp(2, Timestamp.valueOf(day.getDate()));
                        pstmt.setFloat(3, day.getMinTemperature());
                        pstmt.setFloat(4, day.getMaxTemperature());
                        pstmt.setInt(5, day.getHumidity());
                        pstmt.setFloat(6, day.getWindSpeed());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            } else {
                System.err.println("Failed to update forecast due to database connection error.");
            }
        }
    }

    // Lấy dự báo thời tiết từ bảng Forecast
    public List<ForecastDay> getForecast(int cityId) throws SQLException {
        String sql = "SELECT date, min_temperature, max_temperature, humidity, wind_speed FROM Forecast WHERE city_id = ? ORDER BY date";
        List<ForecastDay> forecastDays = new ArrayList<>();

        try (Connection conn = connect()) {
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, cityId);
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        ForecastDay day = new ForecastDay();
                        day.setDate(rs.getTimestamp("date").toLocalDateTime());
                        day.setMinTemperature(rs.getFloat("min_temperature"));
                        day.setMaxTemperature(rs.getFloat("max_temperature"));
                        day.setHumidity(rs.getInt("humidity"));
                        day.setWindSpeed(rs.getFloat("wind_speed"));
                        forecastDays.add(day);
                    }
                }
            }
        }
        return forecastDays;
    }

    // Lấy hoặc thêm cityId cho thành phố
    public int getCityIdByName(String cityName) throws SQLException {
        String sql = "SELECT city_id FROM Cities WHERE city_name = ?";
        try (Connection conn = connect()) {
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, cityName);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt("city_id");
                    } else {
                        return addCityToDatabase(cityName);
                    }
                }
            }
        }
        return -1;
    }

    private int addCityToDatabase(String cityName) throws SQLException {
        String sql = "INSERT INTO Cities (city_name) VALUES (?)";
        try (Connection conn = connect()) {
            if (conn != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, cityName);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        ResultSet generatedKeys = pstmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            return generatedKeys.getInt(1);
                        }
                    }
                }
            }
        }
        return -1;
    }

    public CurrentWeather getCurrentWeather(int cityId) throws SQLException {
        String sql = "SELECT temperature, humidity, wind_speed FROM CurrentWeather WHERE city_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cityId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CurrentWeather(cityId, rs.getDouble("temperature"), rs.getInt("humidity"), rs.getDouble("wind_speed"));
            }
        }
        return null;
    }

    public void updateCurrentWeather(int cityId, double temperature, int humidity, double windSpeed) throws SQLException {
        String sql = "INSERT INTO CurrentWeather (city_id, temperature, humidity, wind_speed) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE temperature = VALUES(temperature), humidity = VALUES(humidity), wind_speed = VALUES(wind_speed)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cityId);
            pstmt.setDouble(2, temperature);
            pstmt.setInt(3, humidity);
            pstmt.setDouble(4, windSpeed);
            pstmt.executeUpdate();
        }
    }
}
