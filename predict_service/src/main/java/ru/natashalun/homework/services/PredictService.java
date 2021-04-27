package ru.natashalun.homework.services;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.natashalun.homework.entities.Currency;
import ru.natashalun.homework.entities.Weather;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PredictService {
    public Currency getPrediction() {
        List<Weather> weather = getLastWeather(8);
        List<Currency> currencies = getLastStocks(7);

        Collections.sort(weather);
        Collections.sort(currencies);

        float[] weatherMapped = new float[8];
        for (int i = 0; i < weather.size(); i++) {
            Weather w = weather.get(i);
            weatherMapped[i] = w.getAvgHumidity() + w.getAvgTemp() + w.getAvgVisibility() + w.getMaxTemp()
                    + w.getMaxWind() + w.getMinTemp() + w.getTotalPrecipitation() + w.getUvIndex();
        }

        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < currencies.size(); i++) {
            regression.addData(weatherMapped[i], currencies.get(i).getRate());
        }
        Currency t = currencies.get(0);
        return new Currency(
                t.getName(),
                (float) regression.predict(weatherMapped[7]),
                LocalDate.now().plusDays(1)
        );
    }

    List<Weather> getLastWeather(int n) {
        ResponseEntity<Weather[]> response =
                new RestTemplate().getForEntity(
                        "http://localhost:8081/weather/?days=" + n,
                        Weather[].class);
        Weather[] weather = response.getBody();
        return Arrays.asList(weather);
    }

    List<Currency> getLastStocks(int n) {
        ResponseEntity<Currency[]> response =
                new RestTemplate().getForEntity(
                        "http://localhost:8080/currency/" + n,
                        Currency[].class);
        Currency[] currency = response.getBody();
        return Arrays.asList(currency);
    }
}
