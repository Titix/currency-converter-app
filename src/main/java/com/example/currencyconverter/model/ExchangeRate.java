package com.example.currencyconverter.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRate {
    private String currency;
    private BigDecimal rate;
    private LocalDate date;
    private String unit;

    public ExchangeRate() {}

    public ExchangeRate(String currency, BigDecimal rate, LocalDate date, String unit) {
        this.currency = currency;
        this.rate = rate;
        this.date = date;
        this.unit = unit;
    }

    // Getters and setters
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "currency='" + currency + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                ", unit='" + unit + '\'' +
                '}';
    }
}
