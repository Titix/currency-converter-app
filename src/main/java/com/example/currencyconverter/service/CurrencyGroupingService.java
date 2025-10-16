package com.example.currencyconverter.service;

import com.example.currencyconverter.model.ExchangeRate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurrencyGroupingService {

    private static final Map<String, List<String>> CURRENCY_GROUPS = new LinkedHashMap<>();
    
    static {
        // European currencies
        CURRENCY_GROUPS.put("European", Arrays.asList(
            "HUF", "EUR", "GBP", "CHF", "CZK", "PLN", "BGN", "HRK", "RSD", 
            "UAH", "RUB", "TRY", "NOK", "SEK", "DKK", "ISK", "RON"
        ));
        
        // Asian currencies
        CURRENCY_GROUPS.put("Asian", Arrays.asList(
            "JPY", "CNY", "INR", "KRW", "SGD", "HKD", "TWD", "THB", "MYR", 
            "IDR", "PHP", "ILS"
        ));
        
        // American currencies
        CURRENCY_GROUPS.put("American", Arrays.asList(
            "USD", "CAD", "BRL", "MXN", "ARS", "CLP", "COP", "PEN"
        ));
        
        // Oceanian currencies
        CURRENCY_GROUPS.put("Oceanian", Arrays.asList(
            "AUD", "NZD"
        ));
        
        // African currencies
        CURRENCY_GROUPS.put("African", Arrays.asList(
            "ZAR"
        ));
    }

    public Map<String, List<ExchangeRate>> groupCurrenciesByRegion(List<ExchangeRate> currencies) {
        Map<String, List<ExchangeRate>> groupedCurrencies = new LinkedHashMap<>();
        
        // Initialize all groups
        for (String region : CURRENCY_GROUPS.keySet()) {
            groupedCurrencies.put(region, new ArrayList<>());
        }
        
        // Add currencies to their respective groups
        for (ExchangeRate currency : currencies) {
            String currencyCode = currency.getCurrency();
            
            for (Map.Entry<String, List<String>> entry : CURRENCY_GROUPS.entrySet()) {
                if (entry.getValue().contains(currencyCode)) {
                    groupedCurrencies.get(entry.getKey()).add(currency);
                    break;
                }
            }
        }
        
        // Remove empty groups
        groupedCurrencies.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        
        return groupedCurrencies;
    }
    
    public String getRegionForCurrency(String currencyCode) {
        for (Map.Entry<String, List<String>> entry : CURRENCY_GROUPS.entrySet()) {
            if (entry.getValue().contains(currencyCode)) {
                return entry.getKey();
            }
        }
        return "Other";
    }
}
