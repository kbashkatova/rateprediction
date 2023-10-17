package ru.liga.rateforecaster.forecast.algorithm;

import ru.liga.rateforecaster.model.CurrencyData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * A utility class for calculating currency rates.
 */
public  class RateCalculator {

    private static final Logger logger = LoggerFactory.getLogger(RateCalculator.class);

    /**
     * Calculate the currency rate for a specific date.
     *
     * @param currencyDataList The list of currency data to search for the rate.
     * @param targetDate       The target date for which to calculate the rate.
     * @return The currency data for the specified date or null if not found.
     */
    public static CurrencyData calculateRateForDate(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        return currencyDataList.stream()
                .filter(data -> data.getDate().isEqual(targetDate))
                .findFirst()
                .orElse(null);
    }
    /**
     * Calculate the average currency rate from the last seven rates before the specified date.
     *
     * @param currencyDataList The list of currency data to calculate the average rate.
     * @param currentDate      The current date for which to calculate the average.
     * @return The average currency rate or 0.0 if there are not enough data points.
     */
    public static double calculateAverageRateFromLastSevenRates(List<CurrencyData> currencyDataList, LocalDate currentDate) {
        try {
            List<CurrencyData> previousData = currencyDataList.stream()
                    .filter(data -> data.getRate() > 0)
                    .filter(data -> data.getDate().isBefore(currentDate))
                    .limit(7)
                    .toList();

            if (previousData.size() < 7) {
                logger.warn("Insufficient data for calculating a 7-day average rate.");
                return 0.0;
            }

            return previousData.stream()
                    .mapToDouble(CurrencyData::getRate)
                    .average()
                    .orElse(0.0);
        } catch (Exception e) {
            logger.error("Failed to calculate the average rate: {}", e.getMessage(), e);
            return 0.0;
        }
    }
}