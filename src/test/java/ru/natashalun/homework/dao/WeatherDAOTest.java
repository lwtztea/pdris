package ru.natashalun.homework.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.natashalun.homework.entities.Weather;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WeatherDAOTest {

    private static final String CITY = "Moscow";

    @Autowired
    private WeatherDAO weatherDAO;

    @Test
    public void getWeatherForToday() {
        List<Weather> result = weatherDAO.getLastWeather(1, CITY);
        assertEquals(1, result.size());
        assertEquals(LocalDate.now(), result.get(0).getDate());
    }

    @Test
    public void getWeatherHistory() {
        List<Weather> result = weatherDAO.getLastWeather(5, CITY);
        assertEquals(5, result.size());
    }

    @Test
    public void getNegativeWeatherHistory() {
        List<Weather> result = weatherDAO.getLastWeather(-5, CITY);
        assertEquals(0, result.size());
    }
}