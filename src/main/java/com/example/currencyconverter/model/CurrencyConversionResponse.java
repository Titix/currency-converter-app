package com.example.currencyconverter.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrencyConversionResponse {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal originalAmount;
    private BigDecimal convertedAmount;
    private BigDecimal exchangeRate;
    private LocalDate rateDate;
    private String message;

    public CurrencyConversionResponse() {}

    public CurrencyConversionResponse(String fromCurrency, String toCurrency, BigDecimal originalAmount, 
                                    BigDecimal convertedAmount, BigDecimal exchangeRate, LocalDate rateDate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
        this.exchangeRate = exchangeRate;
        this.rateDate = rateDate;
    }

    // Getters and setters
    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDate getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CurrencyConversionResponse{" +
                "fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", originalAmount=" + originalAmount +
                ", convertedAmount=" + convertedAmount +
                ", exchangeRate=" + exchangeRate +
                ", rateDate=" + rateDate +
                ", message='" + message + '\'' +
                '}';
    }
}
