
package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduledWeatherUpdate {

    public static void main(String[] args) {
        Timer timer = new Timer();
        WeatherService weatherService = new WeatherService();

        // Danh sách các thành phố để theo dõi, thêm tên thành phố vào danh sách này
        List<String> cityNames = Arrays.asList("London", "New York", "Tokyo", "Paris","Hanoi");

        // Lên lịch cập nhật thời tiết hiện tại mỗi 5 phút
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (String cityName : cityNames) {
                    try {
                        // Lấy và lưu dữ liệu thời tiết hiện tại cho từng thành phố
                        weatherService.getCurrentWeather(cityName);
                    } catch (Exception e) {
                        System.err.println("Failed to update current weather for city: " + cityName);
                        e.printStackTrace();
                    }
                }
                System.out.println("Current weather data updated for cities: " + cityNames);
            }
        }, 0, 300000); // Cập nhật mỗi 5 phút

        // Lên lịch cập nhật dự báo thời tiết mỗi 3 giờ
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (String cityName : cityNames) {
                    try {
                        // Lấy và lưu dự báo 5 ngày cho từng thành phố
                        weatherService.getForecast(cityName);
                    } catch (Exception e) {
                        System.err.println("Failed to update 5-day forecast for city: " + cityName);
                        e.printStackTrace();
                    }
                }
                System.out.println("5-day forecast data updated for cities: " + cityNames);
            }
        }, 0, 10800000); // Cập nhật mỗi 3 giờ (10800000 ms)
    }
}
