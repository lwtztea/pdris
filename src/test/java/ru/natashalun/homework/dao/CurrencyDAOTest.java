package ru.natashalun.homework.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.natashalun.homework.entities.Currency;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CurrencyDAOTest {

    @Autowired
    private CurrencyDAO currencyDAO;

    @Test
    public void getStockForToday() {
        List<Currency> result = currencyDAO.getLastExchangeRates(1);
        assertEquals(1, result.size());
        assertEquals(LocalDate.now(), result.get(0).getDate());
    }

    @Test
    public void getStocks() {
        List<Currency> result = currencyDAO.getLastExchangeRates(5);
        assertEquals(5, result.size());
    }

    @Test
    public void getNegativeStocks() {
        List<Currency> result = currencyDAO.getLastExchangeRates(-5);
        assertEquals(0, result.size());
    }
}