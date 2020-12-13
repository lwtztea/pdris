package ru.natashalun.homework.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.natashalun.homework.entities.Weather;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    public void getTodayWeather() {
        Weather result = weatherService.getTodayWeather();
        assertEquals(LocalDate.now(), result.getDate());
    }

    @Test
    public void getTodayWeatherWithCity() {
        Weather result = weatherService.getTodayWeather("Yaroslavl");
        assertEquals(LocalDate.now(), result.getDate());
    }

    @Test
    public void getWeatherForManyDays() {
        List<Weather> result = weatherService.getLastWeather(5);
        assertEquals(5, result.size());
        Collections.sort(result);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(LocalDate.now().minusDays(result.size() - i - 1), result.get(i).getDate());
        }
    }

    @Test
    public void getWeatherForManyDaysWithCity() {
        List<Weather> result = weatherService.getLastWeather(5, "Yaroslavl");
        assertEquals(5, result.size());
        Collections.sort(result);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(LocalDate.now().minusDays(result.size() - i - 1), result.get(i).getDate());
        }
    }

    @Test
    public void isWeatherSortingIsCorrect() {
        List<Weather> result = weatherService.getLastWeather(5);
        Collections.sort(result);
        assertTrue(result.get(4).compareTo(result.get(0)) > 0);
    }

    @Test
    public void getWeatherForNegativeDays() {
        List<Weather> result = weatherService.getLastWeather(-1);
        assertTrue(result.isEmpty());
    }
}