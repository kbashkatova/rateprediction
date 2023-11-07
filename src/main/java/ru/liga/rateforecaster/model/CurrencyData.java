package ru.liga.rateforecaster.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A class representing currency data with a specific date and rate.
 */
public record CurrencyData(LocalDate date, BigDecimal rate) {

}
