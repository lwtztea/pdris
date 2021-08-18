package ru.natashalun.homework.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Weather implements Comparable<Weather> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private float maxTemp;
    private float minTemp;
    private float avgTemp;
    private float maxWind;
    private float totalPrecipitation;
    private float avgVisibility;
    private int avgHumidity;
    private float uvIndex;
    private LocalDate date;
    private String city;

    public Weather() {
    }

    public Weather(float maxTemp, float minTemp, float avgTemp, float maxWind, float totalPrecipitation, float avgVisibility, int avgHumidity, float uvIndex, LocalDate date) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.avgTemp = avgTemp;
        this.maxWind = maxWind;
        this.totalPrecipitation = totalPrecipitation;
        this.avgVisibility = avgVisibility;
        this.avgHumidity = avgHumidity;
        this.uvIndex = uvIndex;
        this.date = date;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getAvgTemp() {
        return avgTemp;
    }

    public float getMaxWind() {
        return maxWind;
    }

    public float getTotalPrecipitation() {
        return totalPrecipitation;
    }

    public float getAvgVisibility() {
        return avgVisibility;
    }

    public int getAvgHumidity() {
        return avgHumidity;
    }

    public float getUvIndex() {
        return uvIndex;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int compareTo(Weather weather) {
        return date.compareTo(weather.date);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                ", avgTemp=" + avgTemp +
                ", maxWind=" + maxWind +
                ", totalPrecipitation=" + totalPrecipitation +
                ", avgVisibility=" + avgVisibility +
                ", avgHumidity=" + avgHumidity +
                ", uvIndex=" + uvIndex +
                ", date=" + date +
                ", city='" + city + '\'' +
                '}';
    }
}
