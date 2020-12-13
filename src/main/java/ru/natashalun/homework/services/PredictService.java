package ru.natashalun.homework.services;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.natashalun.homework.entities.Currency;
import ru.natashalun.homework.entities.Weather;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class PredictService {
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private CurrencyService currencyService;

    public Currency getPrediction() {
        List<Weather> weather = weatherService.getLastWeather(8);
        List<Currency> currencies = currencyService.getLastStocks(7);

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
}
