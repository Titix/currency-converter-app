package com.example.currencyconverter.controller;

import com.example.currencyconverter.model.ExchangeRate;
import com.example.currencyconverter.service.CurrencyConversionService;
import com.example.currencyconverter.service.CurrencyGroupingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class WebController {

    @Autowired
    private CurrencyConversionService currencyConversionService;
    
    @Autowired
    private CurrencyGroupingService currencyGroupingService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            Map<String, List<ExchangeRate>> groupedCurrencies = currencyConversionService.getGroupedCurrencies();
            List<ExchangeRate> currencies = currencyConversionService.getAvailableCurrencies();
            
            // Always create currencyRegions map to avoid template errors
            Map<String, String> currencyRegions = new HashMap<>();
            if (!currencies.isEmpty()) {
                for (ExchangeRate currency : currencies) {
                    String region = currencyGroupingService.getRegionForCurrency(currency.getCurrency());
                    currencyRegions.put(currency.getCurrency(), region.toLowerCase());
                }
            }
            model.addAttribute("currencyRegions", currencyRegions);
            
            if (groupedCurrencies.isEmpty()) {
                model.addAttribute("error", "Exchange rate service is currently unavailable. Unable to fetch current exchange rates from Magyar Nemzeti Bank. Please try again later.");
            } else {
                model.addAttribute("groupedCurrencies", groupedCurrencies);
                model.addAttribute("currencies", currencies);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load currencies: " + e.getMessage());
            model.addAttribute("currencyRegions", new HashMap<String, String>());
        }
        return "index";
    }
}
