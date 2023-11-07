package ru.liga.rateforecaster.forecast.algorithm.average;

import ru.liga.rateforecaster.exception.DataFillFailureException;
import ru.liga.rateforecaster.exception.InvalidPredictionDataException;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.utils.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * The AveragePredictionAlgorithm class represents an algorithm for calculating currency rates based on a 7-day average.
 * It extends the RatePredictionAlgorithm class and provides the logic for calculating rates and filling missing data.
 */
public class AveragePredictionAlgorithm extends RatePredictionAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(AveragePredictionAlgorithm.class);
    private static final int AVERAGE_CALCULATION_WINDOW = 7;

    /**
     * Calculates the currency rate for the specified date using a 7-day average rate.
     *
     * @param currencyData  The list of currency data for which the rate is calculated.
     * @param targetDate    The target date for rate calculation.
     * @return The currency data for the specified date.
     */
    @Override
    public CurrencyData calculateRateForDate(List<CurrencyData> currencyData, LocalDate targetDate) {
        if (currencyData == null || currencyData.isEmpty()) {
            throw new IllegalArgumentException("currencyData cannot be null or empty.");
        }
        BigDecimal rateForDate;
        LinkedList<CurrencyData> currencyDataLinkedList = new LinkedList<>(currencyData);
        if (nonNull(currencyData.get(0))) {
            currencyData = fillMissingDates(currencyDataLinkedList, currencyData.get(0).date(), targetDate);
            rateForDate = getAverage(currencyData, targetDate);
        } else {
            throw new IllegalArgumentException("currencyData's first element is null.");
        }


        if (rateForDate != null) {
            return currencyData.stream().filter(data -> data.date().equals(targetDate)).findAny().orElseThrow(
                    () -> new InvalidPredictionDataException("Failed to calculate the rate for the specified date"));
        } else {
            throw new InvalidPredictionDataException("Failed to calculate the rate for the specified date");
        }
    }

    private BigDecimal getAverage(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        try {
            final List<CurrencyData> previousData = currencyDataList.stream()
                    .filter(data -> data.rate().compareTo(BigDecimal.ZERO) > 0 &&
                            data.date().isBefore(targetDate))
                    .limit(AVERAGE_CALCULATION_WINDOW)
                    .toList();

            if (previousData.size() < AVERAGE_CALCULATION_WINDOW) {
                logger.warn("Insufficient data for calculating based on 7-day average rate.");
                throw new InvalidPredictionDataException("Insufficient data for calculating based on 7-day average rate.");
            }

            return previousData.stream()
                    .map(CurrencyData::rate)
                    .filter(rate -> rate.compareTo(BigDecimal.ZERO) > 0)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(previousData.size()), 2, RoundingMode.HALF_UP);
        } catch (RuntimeException e) {
            logger.error("Failed to calculate the rate for the specified date: {}", e.getMessage(), e);
            throw new InvalidPredictionDataException("Failed to calculate the rate for the specified date");
        }
    }



    private LinkedList<CurrencyData> fillMissingDates(LinkedList<CurrencyData> currencyDataList,
                                                     LocalDate currentCurrencyDataListDate,
                                                     LocalDate targetDate) {
        try {
            if (currentCurrencyDataListDate.isEqual(targetDate.plusDays(1))) {
                return currencyDataList;
            } else {
                final BigDecimal averageRate = getAverage(currencyDataList, currentCurrencyDataListDate);
                currencyDataList.addFirst(new CurrencyData(currentCurrencyDataListDate, averageRate));
                final LocalDate nextDate = DateUtils.getNextDate(currentCurrencyDataListDate);
                final List<CurrencyData> filledCurrencyData = fillMissingDates(currencyDataList, nextDate, targetDate);
                final LinkedList<CurrencyData> result = new LinkedList<>();
                result.add(new CurrencyData(currentCurrencyDataListDate, averageRate));
                result.addAll(filledCurrencyData);
                return result;
            }
        } catch (RuntimeException e) {
            logger.error("Failed to fill missing dates: {}", e.getMessage(), e);
            throw new DataFillFailureException("Failed to fill missing dates for average forecast generation.");
        }
    }
}