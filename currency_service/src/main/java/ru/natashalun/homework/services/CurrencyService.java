package ru.natashalun.homework.services;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.natashalun.homework.entities.Currency;
import ru.natashalun.homework.dao.CurrencyDAO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyDAO currencyDAO;

    public Currency getTodayStockExchange() {
        return getLastStocks(1).get(0);
    }

    public List<Currency> getLastStocks(int n) {
        ArrayList<Currency> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            LocalDate date = LocalDate.now().minusDays(i);

            Currency stored = currencyDAO.findCurrencyByDate(LocalDate.now());
            if (stored != null) {
                result.add(stored);
                continue;
            }

            String uri = String.format(
                    "http://www.cbr.ru/scripts/XML_daily.asp?date_req=%s",
                    date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            //Я то это переделала на RestTemplate, только теперь появилась куча ненужных классов ¯\_(ツ)_/¯
            RestTemplate template = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
            converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_XML));
            messageConverters.add(converter);
            template.setMessageConverters(messageConverters);

            ResponseEntity<CurrencyListXML> responseEntity = template.getForEntity(
                    uri,
                    CurrencyListXML.class
            );

            Currency localResult = null;
            if (responseEntity.getBody() != null) {
                for (CurrencyListXML.CurrencyXML item : responseEntity.getBody().currencyList) {
                    if (item.name.equals("Доллар США")) {
                        localResult = item.toCurrency(date);
                    }
                }
            }

            if (localResult == null) {
                continue;
            }
            currencyDAO.save(localResult);
            result.add(localResult);
        }
        return result;
    }

    private static class CurrencyListXML {
        @JacksonXmlProperty(localName = "Valute")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<CurrencyXML> currencyList;

        static class CurrencyXML {
            @JacksonXmlProperty(localName = "Name")
            private String name;
            @JacksonXmlProperty(localName = "Nominal")
            private int nominal;
            @JacksonXmlProperty(localName = "Value")
            private float value;

            public void setValue(String value) {
                this.value = Float.parseFloat(value.replace(',', '.'));
            }

            public Currency toCurrency(LocalDate date) {
                return new Currency(
                        name,
                        value / nominal,
                        date
                );
            }
        }
    }
}
