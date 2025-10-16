package com.example.currencyconverter.controller;

import com.example.currencyconverter.model.CurrencyConversionRequest;
import com.example.currencyconverter.model.CurrencyConversionResponse;
import com.example.currencyconverter.model.ExchangeRate;
import com.example.currencyconverter.service.CurrencyConversionService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<ExchangeRate>> getAvailableCurrencies() {
        try {
            List<ExchangeRate> currencies = currencyConversionService.getAvailableCurrencies();
            return ResponseEntity.ok(currencies);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<List<ExchangeRate>> getExchangeRates() {
        try {
            List<ExchangeRate> rates = currencyConversionService.getAvailableCurrencies();
            return ResponseEntity.ok(rates);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
