package com.example.currencyconverter.service;

import com.example.currencyconverter.model.CurrencyConversionRequest;
import com.example.currencyconverter.model.CurrencyConversionResponse;
import com.example.currencyconverter.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyConversionService {

    @Autowired
    private MnbExchangeRateService mnbExchangeRateService;
    
    @Autowired
    private CurrencyGroupingService currencyGroupingService;

    public CurrencyConversionResponse convertCurrency(CurrencyConversionRequest request) {
        try {
            // Get exchange rates
            List<ExchangeRate> rates = mnbExchangeRateService.getCurrentExchangeRates();
            
            // Find exchange rates for both currencies
            ExchangeRate fromRate = findExchangeRate(rates, request.getFromCurrency());
            ExchangeRate toRate = findExchangeRate(rates, request.getToCurrency());
            
            if (fromRate == null) {
                CurrencyConversionResponse response = new CurrencyConversionResponse();
                response.setMessage("Currency '" + request.getFromCurrency() + "' is not available. Please select from available currencies.");
                return response;
            }
            
            if (toRate == null) {
                CurrencyConversionResponse response = new CurrencyConversionResponse();
                response.setMessage("Currency '" + request.getToCurrency() + "' is not available. Please select from available currencies.");
                return response;
            }
            
            // Convert to HUF first, then to target currency
            BigDecimal amountInHuf = convertToHuf(request.getAmount(), fromRate);
            BigDecimal convertedAmount = convertFromHuf(amountInHuf, toRate);
            
            // Calculate the exchange rate between the two currencies
            BigDecimal exchangeRate = toRate.getRate().divide(fromRate.getRate(), 6, RoundingMode.HALF_UP);
            
            CurrencyConversionResponse response = new CurrencyConversionResponse(
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount(),
                convertedAmount,
                exchangeRate,
                LocalDate.now()
            );
            
            response.setMessage("Conversion completed successfully");
            return response;
            
        } catch (Exception e) {
            CurrencyConversionResponse response = new CurrencyConversionResponse();
            response.setMessage("Error during conversion: " + e.getMessage());
            return response;
        }
    }

    private ExchangeRate findExchangeRate(List<ExchangeRate> rates, String currency) {
        if ("HUF".equals(currency)) {
            // HUF is the base currency, create a mock rate
            ExchangeRate hufRate = new ExchangeRate();
            hufRate.setCurrency("HUF");
            hufRate.setRate(BigDecimal.ONE);
            hufRate.setDate(LocalDate.now());
            hufRate.setUnit("1");
            return hufRate;
        }
        
        return rates.stream()
                .filter(rate -> rate.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    private BigDecimal convertToHuf(BigDecimal amount, ExchangeRate fromRate) {
        // Convert from foreign currency to HUF
        return amount.multiply(fromRate.getRate());
    }

    private BigDecimal convertFromHuf(BigDecimal amountInHuf, ExchangeRate toRate) {
        // Convert from HUF to foreign currency
        return amountInHuf.divide(toRate.getRate(), 2, RoundingMode.HALF_UP);
    }

    public List<ExchangeRate> getAvailableCurrencies() {
        try {
            List<ExchangeRate> rates = mnbExchangeRateService.getCurrentExchangeRates();
            
            // Remove any existing HUF entries from MNB data to avoid duplicates
            rates.removeIf(rate -> "HUF".equals(rate.getCurrency()));
            
            // Create HUF as the base currency
            ExchangeRate hufRate = new ExchangeRate();
            hufRate.setCurrency("HUF");
            hufRate.setRate(BigDecimal.ONE);
            hufRate.setDate(LocalDate.now());
            hufRate.setUnit("1");
            
            // Add HUF at the beginning (index 0) to ensure it's always first
            rates.add(0, hufRate);
            
            return rates;
        } catch (Exception e) {
            // Return empty list when MNB API fails - this will be handled by the controller
            return new ArrayList<>();
        }
    }

    public Map<String, List<ExchangeRate>> getGroupedCurrencies() {
        try {
            List<ExchangeRate> rates = mnbExchangeRateService.getCurrentExchangeRates();
            
            // Remove any existing HUF entries from MNB data to avoid duplicates
            rates.removeIf(rate -> "HUF".equals(rate.getCurrency()));
            
            // Create HUF as the base currency
            ExchangeRate hufRate = new ExchangeRate();
            hufRate.setCurrency("HUF");
            hufRate.setRate(BigDecimal.ONE);
            hufRate.setDate(LocalDate.now());
            hufRate.setUnit("1");
            
            // Add HUF at the beginning (index 0) to ensure it's always first
            rates.add(0, hufRate);
            
            // Group currencies by geographical region
            return currencyGroupingService.groupCurrenciesByRegion(rates);
        } catch (Exception e) {
            // Return empty map when MNB API fails - this will be handled by the controller
            return new LinkedHashMap<>();
        }
    }
}
