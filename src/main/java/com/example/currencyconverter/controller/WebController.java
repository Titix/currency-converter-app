package com.example.currencyconverter.controller;

import com.example.currencyconverter.model.ExchangeRate;
import com.example.currencyconverter.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @GetMapping("/")
    public String index(Model model) {
        try {
            List<ExchangeRate> currencies = currencyConversionService.getAvailableCurrencies();
            if (currencies.isEmpty()) {
                model.addAttribute("error", "Exchange rate service is currently unavailable. Unable to fetch current exchange rates from Magyar Nemzeti Bank. Please try again later.");
            } else {
                model.addAttribute("currencies", currencies);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load currencies: " + e.getMessage());
        }
        return "index";
    }
}
