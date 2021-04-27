package ru.natashalun.homework.dao;

import org.springframework.stereotype.Repository;
import ru.natashalun.homework.entities.Currency;

import java.time.LocalDate;
import java.util.HashMap;

@Repository
public class CurrencyDAO {
    private static final HashMap<LocalDate, Currency> currencyCache = new HashMap<>();

    public Currency getExchangeRateForDate(LocalDate date) {
        if (currencyCache.containsKey(date)) {
            return currencyCache.get(date);
        }
        return null;
    }

    public void setExchangeRateForDate(LocalDate date, Currency currency) {
        currencyCache.put(date, currency);
    }
}
