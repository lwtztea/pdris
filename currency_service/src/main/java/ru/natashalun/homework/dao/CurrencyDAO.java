package ru.natashalun.homework.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.natashalun.homework.entities.Currency;

import java.time.LocalDate;

@Repository
public interface CurrencyDAO extends JpaRepository<Currency, Integer> {
    Currency findCurrencyByDate(LocalDate date);
}
