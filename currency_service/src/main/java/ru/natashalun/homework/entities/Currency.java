package ru.natashalun.homework.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Currency implements Comparable<Currency> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private float rate;
    private LocalDate date;

    public Currency() {
    }

    public Currency(String name, float rate, LocalDate date) {
        this.name = name;
        this.rate = rate;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public float getRate() {
        return rate;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public int compareTo(Currency currency) {
        return date.compareTo(currency.date);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                '}';
    }
}
