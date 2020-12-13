package ru.natashalun.homework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.natashalun.homework.dao.CurrencyDAO;
import ru.natashalun.homework.entities.Currency;

import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyDAO currencyDAO;

    public Currency getTodayStockExchange() {
        return currencyDAO.getLastExchangeRates(1).get(0);
    }

    public List<Currency> getLastStocks(int n) {
        return currencyDAO.getLastExchangeRates(n);
    }
}
