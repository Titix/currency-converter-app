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
    private static final String MNB_BASE_URL = "https://www.mnb.hu/webservices/arfolyamok.asmx";
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
                    .header("SOAPAction", "http://www.mnb.hu/webservices/GetCurrentExchangeRates")
                    .bodyValue(soapRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            List<ExchangeRate> rates = parseExchangeRates(response);
            
            // If no rates were parsed (empty response or parsing failed), throw exception
            if (rates.isEmpty()) {
                throw new RuntimeException("No exchange rates received from MNB API");
            }
            
            return rates;
        } catch (Exception e) {
            // Re-throw the exception to be handled by the calling service
            throw new RuntimeException("Failed to fetch exchange rates from MNB API: " + e.getMessage(), e);
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

}
