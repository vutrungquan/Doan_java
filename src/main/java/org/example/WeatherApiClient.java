
package org.example;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherApiClient {

    private static final String API_KEY = "bf29782c5e93fd9265681b05836a7835";
    private static final String CURRENT_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q={city_name}&appid=" + API_KEY;
    private static final String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?q={city_name}&appid=" + API_KEY;
    private static final String CURRENT_WEATHER_COORD_URL = "http://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid=" + API_KEY;
    private static final String FORECAST_COORD_URL = "http://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid=" + API_KEY;
    public static String getWeatherData(String cityName) {
        return fetchData(CURRENT_WEATHER_URL.replace("{city_name}", cityName));
    }
    public static String getWeatherDataByCoordinates(double lat, double lon) {
        String url = CURRENT_WEATHER_COORD_URL.replace("{lat}", String.valueOf(lat)).replace("{lon}", String.valueOf(lon));
        return fetchData(url);
    }
    public static String get5DayForecast(String cityName) {
        return fetchData(FORECAST_URL.replace("{city_name}", cityName));
    }
    public static String get5DayForecastByCoordinates(double lat, double lon) {
        String url = FORECAST_COORD_URL.replace("{lat}", String.valueOf(lat)).replace("{lon}", String.valueOf(lon));
        return fetchData(url);
    }

    private static String fetchData(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("HTTP error code: " + responseCode);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result.toString();
    }

}
