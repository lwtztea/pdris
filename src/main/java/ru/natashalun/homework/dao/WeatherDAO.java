package ru.natashalun.homework.dao;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import ru.natashalun.homework.entities.Weather;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class WeatherDAO {
    private static final HashMap<LocalDate, HashMap<String, Weather>> weatherCache = new HashMap<>();

    public List<Weather> getLastWeather(int n, String city) {
        System.out.printf("%s, %s\n", n, city);
        LocalDate currentDate = LocalDate.now();
        List<Weather> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            LocalDate date = currentDate.minusDays(i);
            if (weatherCache.containsKey(date) && weatherCache.get(date).containsKey(city)) {
                System.out.printf("Getting from local %s\n", date);
                result.add(weatherCache.get(date).get(city));
            } else {
                System.out.printf("Getting from remote %s\n", date);
                String address = String.format(
                        "http://api.weatherapi.com/v1/history.json?key=%s&q=%s&dt=%s",
                        "b08521b2c5f145078eb232719201212",
                        city,
                        date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                );
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(address))
                        .build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenApply(it -> parseWeather(it, date))
                        .thenApply(it -> {
                            HashMap<String, Weather> map;
                            if (weatherCache.containsKey(date)) {
                                map = weatherCache.get(date);
                            } else {
                                map = new HashMap<>();
                            }
                            map.put(city, it);
                            weatherCache.put(date, map);
                            return it;
                        })
                        .thenAccept(result::add)
                        .join();
            }
        }
        return result;
    }

    private Weather parseWeather(String json, LocalDate date) {
        try {
            JSONObject root = new JSONObject(json);
            JSONObject weatherJSON = root.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day");
            return new Weather(
                    weatherJSON.getFloat("maxtemp_c"),
                    weatherJSON.getFloat("mintemp_c"),
                    weatherJSON.getFloat("avgtemp_c"),
                    weatherJSON.getFloat("maxwind_kph"),
                    weatherJSON.getFloat("totalprecip_mm"),
                    weatherJSON.getFloat("avgvis_km"),
                    weatherJSON.getInt("avghumidity"),
                    weatherJSON.getFloat("uv"),
                    date
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
