package ru.liga.rateforecaster.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A class representing currency data with a specific date and rate.
 */
public class CurrencyData {
    private final LocalDate date;
    private final BigDecimal rate;

    public CurrencyData(LocalDate date, BigDecimal rate) {
        this.date = date;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getRate() {
        return rate;
    }

}
