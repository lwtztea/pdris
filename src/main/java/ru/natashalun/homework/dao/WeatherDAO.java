package ru.natashalun.homework.dao;

import org.springframework.stereotype.Repository;
import ru.natashalun.homework.entities.Weather;

import java.time.LocalDate;
import java.util.HashMap;

@Repository
public class WeatherDAO {
    private static final HashMap<LocalDate, HashMap<String, Weather>> weatherCache = new HashMap<>();

    public Weather getWeather(LocalDate date, String city) {
        if (weatherCache.containsKey(date)) {
            return weatherCache.get(date).get(city);
        } else {
            return null;
        }
    }

    public void setWeather(LocalDate date, String city, Weather weather) {
        if (weatherCache.containsKey(date)) {
            weatherCache.get(date).put(city, weather);
        } else {
            HashMap<String, Weather> hashMap = new HashMap<>();
            hashMap.put(city, weather);
            weatherCache.put(date, hashMap);
        }
    }
}
