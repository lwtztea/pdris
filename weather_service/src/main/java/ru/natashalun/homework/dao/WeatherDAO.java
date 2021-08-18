package ru.natashalun.homework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.natashalun.homework.entities.Weather;

import java.time.LocalDate;

@Repository
public interface WeatherDAO extends JpaRepository<Weather, Integer> {
    Weather findWeatherByDateAndCity(LocalDate date, String city);
}
