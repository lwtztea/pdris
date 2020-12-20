package ru.natashalun.homework.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.natashalun.homework.dao.WeatherDAO;
import ru.natashalun.homework.entities.Weather;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final String DEFAULT_CITY = "Moscow";

    @Autowired
    private WeatherDAO weatherDAO;

    public Weather getTodayWeather() {
        return getTodayWeather(DEFAULT_CITY);
    }

    public Weather getTodayWeather(String city) {
        return getLastWeather(1, city).get(0);
    }

    public List<Weather> getLastWeather(int n) {
        return getLastWeather(n, DEFAULT_CITY);
    }

    public List<Weather> getLastWeather(int n, String city) {
        LocalDate currentDate = LocalDate.now();
        List<Weather> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            LocalDate date = currentDate.minusDays(i);
            Weather stored = weatherDAO.getWeather(date, city);
            if (stored != null) {
                result.add(stored);
                continue;
            }

            String address = String.format(
                    "http://api.weatherapi.com/v1/history.json?key=%s&q=%s&dt=%s",
                    "b08521b2c5f145078eb232719201212",
                    city,
                    date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );

            //Я то это переделала на RestTemplate, только теперь появилась куча ненужных классов ¯\_(ツ)_/¯
            ResponseEntity<WeatherResponse> responseEntity = new RestTemplate().getForEntity(
                    address,
                    WeatherResponse.class
            );

            if (responseEntity.getBody() != null) {
                Weather localResult = responseEntity.getBody()
                        .forecast
                        .forecastDays
                        .get(0)
                        .weatherJSON
                        .toWeather(date);
                result.add(localResult);
                weatherDAO.setWeather(date, city, localResult);
            }
        }
        return result;
    }

    private static class WeatherResponse {
        @JsonProperty("forecast")
        private Forecast forecast;

        static class Forecast {
            @JsonProperty("forecastday")
            private List<ForecastDay> forecastDays;

            static class ForecastDay {
                @JsonProperty("day")
                private WeatherJSON weatherJSON;
            }

            public static class WeatherJSON {
                @JsonProperty("maxtemp_c")
                private float maxtempC;
                @JsonProperty("mintemp_c")
                private float mintempC;
                @JsonProperty("avgtemp_c")
                private float avgtempC;
                @JsonProperty("maxwind_kph")
                private float maxwindKph;
                @JsonProperty("totalprecip_mm")
                private float totalprecipMm;
                @JsonProperty("avgvis_km")
                private float avgvisKm;
                @JsonProperty("avghumidity")
                private int avghumidity;
                @JsonProperty("uv")
                private float uv;

                public Weather toWeather(LocalDate date) {
                    return new Weather(
                            maxtempC,
                            mintempC,
                            avgtempC,
                            maxwindKph,
                            totalprecipMm,
                            avgvisKm,
                            avghumidity,
                            uv,
                            date
                    );
                }
            }
        }
    }
}
