package com.example.currencyconverter;

import com.example.currencyconverter.model.CurrencyConversionRequest;
import com.example.currencyconverter.model.CurrencyConversionResponse;
import com.example.currencyconverter.service.CurrencyConversionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CurrencyConverterApplicationTests {

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @Test
    public void contextLoads() {
        // Test that the Spring context loads successfully
    }

    @Test
    public void testCurrencyConversion() {
        // Test currency conversion functionality
        CurrencyConversionRequest request = new CurrencyConversionRequest();
        request.setFromCurrency("EUR");
        request.setToCurrency("USD");
        request.setAmount(new BigDecimal("100.00"));

        CurrencyConversionResponse response = currencyConversionService.convertCurrency(request);

        assertNotNull(response);
        assertNotNull(response.getConvertedAmount());
        assertTrue(response.getConvertedAmount().compareTo(BigDecimal.ZERO) > 0);
        assertEquals("EUR", response.getFromCurrency());
        assertEquals("USD", response.getToCurrency());
    }

    @Test
    public void testGetAvailableCurrencies() {
        // Test getting available currencies
        var currencies = currencyConversionService.getAvailableCurrencies();
        
        assertNotNull(currencies);
        assertFalse(currencies.isEmpty());
        
        // Check that HUF is included as base currency
        boolean hasHuf = currencies.stream()
                .anyMatch(rate -> "HUF".equals(rate.getCurrency()));
        assertTrue(hasHuf);
    }
}
