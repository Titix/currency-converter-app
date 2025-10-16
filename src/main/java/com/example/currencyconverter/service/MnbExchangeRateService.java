package com.example.currencyconverter.service;

import com.example.currencyconverter.model.ExchangeRate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MnbExchangeRateService {

    private final WebClient webClient;
    private static final String MNB_BASE_URL = "https://www.mnb.hu/arfolyamok.asmx";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MnbExchangeRateService() {
        this.webClient = WebClient.builder()
                .baseUrl(MNB_BASE_URL)
                .build();
    }

    public List<ExchangeRate> getCurrentExchangeRates() {
        try {
            String soapRequest = createSoapRequest();
            String response = webClient.post()
                    .header("Content-Type", "text/xml; charset=utf-8")
                    .header("SOAPAction", "http://www.mnb.hu/webservices/")
                    .bodyValue(soapRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseExchangeRates(response);
        } catch (Exception e) {
            // Fallback to mock data if MNB API is not accessible
            return getMockExchangeRates();
        }
    }

    public ExchangeRate getExchangeRate(String currency) {
        List<ExchangeRate> rates = getCurrentExchangeRates();
        return rates.stream()
                .filter(rate -> rate.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    private String createSoapRequest() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<GetCurrentExchangeRates xmlns=\"http://www.mnb.hu/webservices/\">" +
                "</GetCurrentExchangeRates>" +
                "</soap:Body>" +
                "</soap:Envelope>";
    }

    private List<ExchangeRate> parseExchangeRates(String xmlResponse) {
        List<ExchangeRate> rates = new ArrayList<>();
        
        // Parse the XML response to extract exchange rates
        Pattern ratePattern = Pattern.compile("<Rate currency=\"([^\"]+)\">([^<]+)</Rate>");
        Matcher matcher = ratePattern.matcher(xmlResponse);
        
        while (matcher.find()) {
            String currency = matcher.group(1);
            BigDecimal rate = new BigDecimal(matcher.group(2));
            
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setCurrency(currency);
            exchangeRate.setRate(rate);
            exchangeRate.setDate(LocalDate.now());
            exchangeRate.setUnit("1");
            
            rates.add(exchangeRate);
        }
        
        return rates;
    }

    private List<ExchangeRate> getMockExchangeRates() {
        List<ExchangeRate> mockRates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        // Mock exchange rates (HUF as base currency)
        mockRates.add(new ExchangeRate("EUR", new BigDecimal("400.50"), today, "1"));
        mockRates.add(new ExchangeRate("USD", new BigDecimal("370.25"), today, "1"));
        mockRates.add(new ExchangeRate("GBP", new BigDecimal("460.75"), today, "1"));
        mockRates.add(new ExchangeRate("CHF", new BigDecimal("420.30"), today, "1"));
        mockRates.add(new ExchangeRate("JPY", new BigDecimal("2.45"), today, "100"));
        mockRates.add(new ExchangeRate("CAD", new BigDecimal("275.80"), today, "1"));
        mockRates.add(new ExchangeRate("AUD", new BigDecimal("245.60"), today, "1"));
        
        // Additional European currencies
        mockRates.add(new ExchangeRate("RON", new BigDecimal("80.15"), today, "1")); // Romanian Leu
        mockRates.add(new ExchangeRate("CZK", new BigDecimal("16.25"), today, "1")); // Czech Koruna
        mockRates.add(new ExchangeRate("PLN", new BigDecimal("92.40"), today, "1")); // Polish Zloty
        mockRates.add(new ExchangeRate("BGN", new BigDecimal("204.80"), today, "1")); // Bulgarian Lev
        mockRates.add(new ExchangeRate("HRK", new BigDecimal("53.20"), today, "1")); // Croatian Kuna
        mockRates.add(new ExchangeRate("RSD", new BigDecimal("3.40"), today, "100")); // Serbian Dinar
        mockRates.add(new ExchangeRate("UAH", new BigDecimal("9.85"), today, "1")); // Ukrainian Hryvnia
        
        // Additional major world currencies
        mockRates.add(new ExchangeRate("CNY", new BigDecimal("51.20"), today, "1")); // Chinese Yuan
        mockRates.add(new ExchangeRate("INR", new BigDecimal("4.45"), today, "1")); // Indian Rupee
        mockRates.add(new ExchangeRate("BRL", new BigDecimal("75.30"), today, "1")); // Brazilian Real
        mockRates.add(new ExchangeRate("MXN", new BigDecimal("20.15"), today, "1")); // Mexican Peso
        mockRates.add(new ExchangeRate("KRW", new BigDecimal("0.28"), today, "100")); // South Korean Won
        mockRates.add(new ExchangeRate("SGD", new BigDecimal("275.90"), today, "1")); // Singapore Dollar
        mockRates.add(new ExchangeRate("NZD", new BigDecimal("225.40"), today, "1")); // New Zealand Dollar
        mockRates.add(new ExchangeRate("NOK", new BigDecimal("34.20"), today, "1")); // Norwegian Krone
        mockRates.add(new ExchangeRate("SEK", new BigDecimal("33.85"), today, "1")); // Swedish Krona
        mockRates.add(new ExchangeRate("DKK", new BigDecimal("53.70"), today, "1")); // Danish Krone
        
        return mockRates;
    }
}
