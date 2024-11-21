package org.example;
import java.time.LocalDateTime;
public class ForecastDay {
    private LocalDateTime date;
    private float minTemperature;
    private float maxTemperature;
    private int humidity;
    private float windSpeed;
    private String weatherCondition;
    public String getWeatherCondition() {
        return weatherCondition;
    }
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    // Getters
    public LocalDateTime getDate() {
        return date;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    // Setters
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setMinTemperature(float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setMaxTemperature(float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }
}
