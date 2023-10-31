package ru.liga.rateforecaster.forecast.algorithm.year;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.exception.InvalidPredictionDataException;
import ru.liga.rateforecaster.forecast.algorithm.average.AveragePredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The YearPredictionAlgorithm class is responsible for predicting currency rates based on historical data from the same date in the previous year.
 */
public class YearPredictionAlgorithm extends RatePredictionAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(YearPredictionAlgorithm.class);
    private static final int MAX_ATTEMPTS = 2;

    /**
     * Calculates the currency rate for a specific target date based on historical data from the same date in the previous year.
     *
     * @param currencyDataList The list of historical currency data.
     * @param targetDate       The target date for which the rate is predicted.
     * @return The predicted currency rate for the target date.
     */
    @Override
    public CurrencyData calculateRateForDate(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        Optional<CurrencyData> currentYearRate = findTargetDateRate(currencyDataList, targetDate);
        if (currentYearRate.isPresent()) {
            return currentYearRate.get();
        } else {
            try {
                return findLastYearRate(currencyDataList, targetDate);
            } catch (RuntimeException e) {
                logger.error("Failed to calculate the rate for the specified date: {}", e.getMessage(), e);
                throw new InvalidPredictionDataException("Failed to calculate the rate for the specified date");
            }
        }
    }

    private CurrencyData findLastYearRate(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        Optional<CurrencyData> lastYearRate = Optional.empty();
        LocalDate targetDateNew = targetDate;
        while (lastYearRate.isEmpty()) {
            int attempts = 0;
            targetDateNew = DateUtils.getLastYearDate(targetDateNew);
            LocalDate targerDateLetAloneWeekend = targetDateNew;
            lastYearRate = findTargetDateRate(currencyDataList, targerDateLetAloneWeekend);
            if (!lastYearRate.isEmpty()) {
                return new CurrencyData(targetDate, lastYearRate.get().getRate());
            }
            while (attempts <= MAX_ATTEMPTS) {
                lastYearRate = findTargetDateRate(currencyDataList, targerDateLetAloneWeekend);
                if (!lastYearRate.isEmpty()) {
                    return new CurrencyData(targetDate, lastYearRate.get().getRate());
                }
                targerDateLetAloneWeekend = DateUtils.getPreviousDate(targerDateLetAloneWeekend);
                attempts++;
            }
        }
        return lastYearRate.get();
    }

    private Optional<CurrencyData> findTargetDateRate(List<CurrencyData> currencyDataList, LocalDate date) {
        return Optional.ofNullable(currencyDataList.stream()
                .filter(data -> data.getDate().isEqual(date))
                .findFirst().orElseGet(() -> null));
    }
}