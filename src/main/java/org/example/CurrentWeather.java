package org.example;

public class CurrentWeather {
    private int cityId;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private String weatherCondition;
    public CurrentWeather(int cityId, double temperature, double humidity, double windSpeed) {
        this.cityId = cityId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    // Getter và Setter cho temperature
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    // Getter và Setter cho humidity
    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    // Getter và Setter cho windSpeed
    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}
