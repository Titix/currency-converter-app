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
    private static final String MNB_BASE_URL = "http://www.mnb.hu/arfolyamok.asmx";
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
                    .header("SOAPAction", "http://www.mnb.hu/webservices/MNBArfolyamServiceSoap/GetCurrentExchangeRates")
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
        
        try {
            // Extract the inner XML from the SOAP response
            Pattern resultPattern = Pattern.compile("<GetCurrentExchangeRatesResult>(.*?)</GetCurrentExchangeRatesResult>", Pattern.DOTALL);
            Matcher resultMatcher = resultPattern.matcher(xmlResponse);
            
            if (resultMatcher.find()) {
                String innerXml = resultMatcher.group(1);
                // Decode HTML entities
                innerXml = innerXml.replace("&lt;", "<").replace("&gt;", ">");
                
                // Parse the exchange rates from the inner XML
                Pattern ratePattern = Pattern.compile("<Rate unit=\"([^\"]+)\" curr=\"([^\"]+)\">([^<]+)</Rate>");
                Matcher rateMatcher = ratePattern.matcher(innerXml);
                
                while (rateMatcher.find()) {
                    String unit = rateMatcher.group(1);
                    String currency = rateMatcher.group(2);
                    String rateStr = rateMatcher.group(3).replace(",", "."); // Replace comma with dot for decimal parsing
                    BigDecimal rate = new BigDecimal(rateStr);
                    
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setCurrency(currency);
                    exchangeRate.setRate(rate);
                    exchangeRate.setDate(LocalDate.now());
                    exchangeRate.setUnit(unit);
                    
                    rates.add(exchangeRate);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing MNB response: " + e.getMessage());
            System.err.println("Response was: " + xmlResponse);
        }
        
        return rates;
    }

}
