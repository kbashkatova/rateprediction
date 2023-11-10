package ru.liga.rateforecaster.forecast.algorithm.year;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.exception.InvalidPredictionDataException;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
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
        return findTargetDateRate(currencyDataList, targetDate)
                .orElseGet(() -> getDefaultCurrencyData(currencyDataList, targetDate));
    }

    @NotNull
    private CurrencyData getDefaultCurrencyData(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        try {
            return findLastYearRate(currencyDataList, targetDate);
        } catch (InvalidPredictionDataException e) {
            logger.error("Failed to calculate the rate for the specified date: {}", e.getMessage());
            throw new InvalidPredictionDataException("Failed to calculate the rate for the specified date");
        }
    }

    private CurrencyData findLastYearRate(List<CurrencyData> currencyDataList, LocalDate targetDate) {

        if (currencyDataList.isEmpty()) {
            throw new InvalidPredictionDataException("Failed to calculate the rate for the specified date");
        }

        Optional<CurrencyData> lastYearRate = Optional.empty();
        LocalDate targetDateNew = targetDate;
        int maxAttemptsAccordingToNumberOfYears = calculateYearDifferenceBetweenDates(currencyDataList, targetDate);
        for (int yearIterationAttempts = 0; yearIterationAttempts <= maxAttemptsAccordingToNumberOfYears;
             yearIterationAttempts++) {
            int attemptsDueToEmptyWeekendRate = 0;
            targetDateNew = DateUtils.getLastYearDate(targetDateNew);
            LocalDate targerDateLetAloneWeekend = targetDateNew;
            lastYearRate = findTargetDateRate(currencyDataList, targerDateLetAloneWeekend);
            if (lastYearRate.isPresent()) {
                return new CurrencyData(targetDate, lastYearRate.get().rate());
            }
            while (attemptsDueToEmptyWeekendRate <= MAX_ATTEMPTS) {
                lastYearRate = findTargetDateRate(currencyDataList, targerDateLetAloneWeekend);
                if (lastYearRate.isPresent()) {
                    return new CurrencyData(targetDate, lastYearRate.get().rate());
                }
                targerDateLetAloneWeekend = DateUtils.getPreviousDate(targerDateLetAloneWeekend);
                attemptsDueToEmptyWeekendRate++;
            }
        }
        throw new InvalidPredictionDataException("Failed to find the exchange rate for the specified date.");
    }

    private Optional<CurrencyData> findTargetDateRate(List<CurrencyData> currencyDataList, LocalDate date) {
        return currencyDataList.stream()
                .filter(data -> data.date().isEqual(date))
                .findFirst();
    }

    private int calculateYearDifferenceBetweenDates(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        LocalDate oldestDate = currencyDataList.stream()
                .map(CurrencyData::date)
                .min(LocalDate::compareTo)
                .orElse(targetDate);
        return Period.between(oldestDate, targetDate).getYears();
    }

}