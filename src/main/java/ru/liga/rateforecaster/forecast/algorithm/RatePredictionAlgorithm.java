package ru.liga.rateforecaster.forecast.algorithm;

import ru.liga.rateforecaster.model.CurrencyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * The RatePredictionAlgorithm class is an abstract base class for rate prediction algorithms.
 * It provides common methods for retrieving a rate for a specific date and calculating rates for a given date.
 */
public abstract class RatePredictionAlgorithm {


    /**
     * Retrieves the rate data for the specified target date from the given list of currency data.
     *
     * @param currencyDataList The list of currency data to search for the rate.
     * @param targetDate       The target date for which the rate is sought.
     * @return The currency data for the specified target date, or null if not found.
     */
    public CurrencyData getRateForDate(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        return currencyDataList.stream()
                .filter(data -> data.getDate().isEqual(targetDate))
                .findFirst()
                .orElse(null);
    }
    /**
     * Calculates the rate for the specified date using the provided list of currency data.
     *
     * @param currencyDataList The list of currency data for rate calculation.
     * @param currentDate      The date for which the rate should be calculated.
     * @return The calculated currency data for the specified date.
     */
    public abstract CurrencyData calculateRateForDate(List<CurrencyData> currencyDataList, LocalDate currentDate);

}
