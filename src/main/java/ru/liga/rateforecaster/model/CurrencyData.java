package ru.liga.rateforecaster.model;

import java.time.LocalDate;

/**
 * A class representing currency data with a specific date and rate.
 */
public class CurrencyData {
    private LocalDate date;
    private double rate;

    public CurrencyData(LocalDate date, double rate) {
        this.date = date;
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getRate() {
        return rate;
    }

}
