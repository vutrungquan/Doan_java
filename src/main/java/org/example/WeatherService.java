package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherService {
    private final WeatherDatabase db;

    public WeatherService() {
        this.db = new WeatherDatabase();
    }

    public CurrentWeather getCurrentWeather(String cityName) throws SQLException {
        int cityId = db.getCityIdByName(cityName);
        CurrentWeather weather = db.getCurrentWeather(cityId);
        String apiResponse = WeatherApiClient.getWeatherData(cityName);
        if (!apiResponse.isEmpty()) {
            JSONObject obj = new JSONObject(apiResponse);
            String fetchedCityName = obj.getString("name");
            double temp = obj.getJSONObject("main").getDouble("temp") - 273.15;
            int humidity = obj.getJSONObject("main").getInt("humidity");
            double windSpeed = obj.getJSONObject("wind").getDouble("speed");
            String condition = obj.getJSONArray("weather").getJSONObject(0).getString("main");

            cityId = db.getCityIdByName(fetchedCityName);
            db.updateCurrentWeather(cityId, temp, humidity, windSpeed);

            CurrentWeather currentWeather = new CurrentWeather(cityId, temp, humidity, windSpeed);
            currentWeather.setWeatherCondition(condition);
            return currentWeather;
        }
        return weather;
    }

    public List<ForecastDay> getForecast(String cityName) throws SQLException {
        int cityId = db.getCityIdByName(cityName);
        List<ForecastDay> forecastDays = db.getForecast(cityId);
        String apiResponse = WeatherApiClient.get5DayForecast(cityName);
        if (!apiResponse.isEmpty()) {
            forecastDays = parseForecastData(apiResponse);
            db.updateForecast(cityId, forecastDays);
        }
        return forecastDays;
    }

    private List<ForecastDay> parseForecastData(String apiResponse) {
        List<ForecastDay> forecastDays = new ArrayList<>();
        JSONObject obj = new JSONObject(apiResponse);
        JSONArray listArray = obj.getJSONArray("list");

        // Định dạng cho ngày giờ từ chuỗi JSON
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < listArray.length(); i += 8) {
            JSONObject dayObj = listArray.getJSONObject(i);
            ForecastDay day = new ForecastDay();

            // Sử dụng định dạng cụ thể để phân tích ngày giờ
            day.setDate(java.time.LocalDateTime.parse(dayObj.getString("dt_txt"), formatter));
            day.setMinTemperature((float) dayObj.getJSONObject("main").getDouble("temp_min") - 273.15f);
            day.setMaxTemperature((float) dayObj.getJSONObject("main").getDouble("temp_max") - 273.15f);
            day.setHumidity(dayObj.getJSONObject("main").getInt("humidity"));
            day.setWindSpeed((float) dayObj.getJSONObject("wind").getDouble("speed"));
            day.setWeatherCondition(dayObj.getJSONArray("weather").getJSONObject(0).getString("main"));

            forecastDays.add(day);
        }
        return forecastDays;
    }
    public List<ForecastDay> getForecastByCoordinates(double lat, double lon) throws SQLException {
        String apiResponse = WeatherApiClient.get5DayForecastByCoordinates(lat, lon);
        if (apiResponse == null || apiResponse.isEmpty()) {
            System.err.println("No forecast data available for the given coordinates.");
            return new ArrayList<>();
        }
        return parseForecastData(apiResponse);
    }

    public CurrentWeather getCurrentWeatherByCoordinates(double lat, double lon) throws SQLException {
        String apiResponse = WeatherApiClient.getWeatherDataByCoordinates(lat, lon);
        if (!apiResponse.isEmpty()) {
            JSONObject obj = new JSONObject(apiResponse);
            String cityName = obj.getString("name");
            double temp = obj.getJSONObject("main").getDouble("temp") - 273.15;
            int humidity = obj.getJSONObject("main").getInt("humidity");
            double windSpeed = obj.getJSONObject("wind").getDouble("speed");
            String condition = obj.getJSONArray("weather").getJSONObject(0).getString("main");
            int cityId = db.getCityIdByName(cityName);
            db.updateCurrentWeather(cityId, temp, humidity, windSpeed);
            CurrentWeather currentWeather = new CurrentWeather(cityId, temp, humidity, windSpeed);
            currentWeather.setWeatherCondition(condition);
            return new CurrentWeather(cityId, temp, humidity, windSpeed);
        }
        return null;
    }
}