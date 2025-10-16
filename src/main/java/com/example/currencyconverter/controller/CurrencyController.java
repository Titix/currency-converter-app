package com.example.currencyconverter.controller;

import com.example.currencyconverter.model.CurrencyConversionRequest;
import com.example.currencyconverter.model.CurrencyConversionResponse;
import com.example.currencyconverter.model.ExchangeRate;
import com.example.currencyconverter.service.CurrencyConversionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CurrencyController {

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponse> convertCurrency(@Valid @RequestBody CurrencyConversionRequest request) {
        try {
            CurrencyConversionResponse response = currencyConversionService.convertCurrency(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CurrencyConversionResponse errorResponse = new CurrencyConversionResponse();
            errorResponse.setMessage("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/currencies")
    public ResponseEntity<?> getAvailableCurrencies() {
        try {
            List<ExchangeRate> currencies = currencyConversionService.getAvailableCurrencies();
            
            if (currencies.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Exchange rate service is currently unavailable");
                errorResponse.put("message", "Unable to fetch current exchange rates from Magyar Nemzeti Bank. Please try again later.");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
            }
            
            return ResponseEntity.ok(currencies);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Exchange rate service error");
            errorResponse.put("message", "An error occurred while fetching exchange rates: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<?> getExchangeRates() {
        try {
            List<ExchangeRate> rates = currencyConversionService.getAvailableCurrencies();
            
            if (rates.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Exchange rate service is currently unavailable");
                errorResponse.put("message", "Unable to fetch current exchange rates from Magyar Nemzeti Bank. Please try again later.");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
            }
            
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Exchange rate service error");
            errorResponse.put("message", "An error occurred while fetching exchange rates: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
