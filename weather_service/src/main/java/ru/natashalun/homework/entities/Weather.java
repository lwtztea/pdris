package ru.natashalun.homework.entities;

import java.time.LocalDate;

public class Weather implements Comparable<Weather> {
    private final float maxTemp;
    private final float minTemp;
    private final float avgTemp;
    private final float maxWind;
    private final float totalPrecipitation;
    private final float avgVisibility;
    private final int avgHumidity;
    private final float uvIndex;
    private final LocalDate date;

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
                '}';
    }
}
