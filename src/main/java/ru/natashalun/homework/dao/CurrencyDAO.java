package ru.natashalun.homework.dao;

import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.natashalun.homework.entities.Currency;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class CurrencyDAO {
    private static final HashMap<LocalDate, Currency> currencyCache = new HashMap<>();

    public List<Currency> getLastExchangeRates(int n) {
        LocalDate currentDate = LocalDate.now();
        List<Currency> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            LocalDate date = currentDate.minusDays(i);
            if (currencyCache.containsKey(date)) {
                System.out.printf("Getting from local %s\n", date);
                result.add(currencyCache.get(date));
            } else {
                System.out.printf("Getting from remote %s\n", date);
                String address = String.format(
                        "http://www.cbr.ru/scripts/XML_daily.asp?date_req=%s",
                        date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(address))
                        .build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenApply(it -> parseDollarFromXml(it, date))
                        .thenApply(it -> {
                            currencyCache.put(date, it);
                            return it;
                        })
                        .thenAccept(result::add)
                        .join();
            }
        }
        return result;
    }

    private Currency parseDollarFromXml(String xml, LocalDate date) {
        try {
            final String DOLLAR_CODE = "840";
            final String NumCodeKey = "NumCode";
            final String NominalKey = "Nominal";
            final String NameKey = "Name";
            final String ValueKey = "Value";
            boolean dollarParsed = false;
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            
            //REDHAT
            //https://www.blackhat.com/docs/us-15/materials/us-15-Wang-FileCry-The-New-Age-Of-XXE-java-wp.pdf
            factory.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            //OWASP
            //https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            // Disable external DTDs as well
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList list = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                NodeList mainItemList = list.item(i).getChildNodes();
                int nominal = 0;
                float value = 0;
                String name = "";
                for (int j = 0; j < mainItemList.getLength(); j++) {
                    Node item = mainItemList.item(j);
                    if (item.getNodeName().equals(NumCodeKey)) {
                        if (!item.getTextContent().equals(DOLLAR_CODE)) {
                            break;
                        } else {
                            dollarParsed = true;
                        }
                    }
                    if (item.getNodeName().equals(NominalKey)) {
                        nominal = Integer.parseInt(item.getTextContent());
                    }
                    if (item.getNodeName().equals(ValueKey)) {
                        value = Float.parseFloat(item.getTextContent().replace(',', '.'));
                    }
                    if (item.getNodeName().equals(NameKey)) {
                        name = item.getTextContent();
                    }
                }
                if (dollarParsed && nominal != 0) {
                    return new Currency(name, value / nominal, date);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Logger.getAnonymousLogger().log(Level.ALL, "Error", e);
            return null;
        }
        return null;
    }
}
