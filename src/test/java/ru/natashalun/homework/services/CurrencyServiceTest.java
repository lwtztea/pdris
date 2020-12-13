package ru.natashalun.homework.services;

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
class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @Test
    public void getTodayStock() {
        Currency result = currencyService.getTodayStockExchange();
        assertEquals(LocalDate.now(), result.getDate());
    }

    @Test
    public void getStocksForManyDays() {
        List<Currency> result = currencyService.getLastStocks(5);
        assertEquals(5, result.size());
    }

    @Test
    public void getStocksForNegativeDays() {
        List<Currency> result = currencyService.getLastStocks(-1);
        assertTrue(result.isEmpty());
    }
}