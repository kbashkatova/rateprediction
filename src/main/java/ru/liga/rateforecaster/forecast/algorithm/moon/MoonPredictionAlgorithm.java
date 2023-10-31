package ru.liga.rateforecaster.forecast.algorithm.moon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.exception.InvalidPredictionDataException;
import ru.liga.rateforecaster.forecast.algorithm.average.AveragePredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The MoonPredictionAlgorithm class is responsible for predicting currency rates based on the moon phases.
 */
public class MoonPredictionAlgorithm extends RatePredictionAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(MoonPredictionAlgorithm.class);
    private static final int DAYS_IN_MONTH = 30;

    /**
     * Calculates the currency rate for a specific target date using moon phase prediction.
     *
     * @param currencyDataList The list of historical currency data.
     * @param targetDate       The target date for which the rate is predicted.
     * @return The predicted currency rate for the target date.
     */
    @Override
    public CurrencyData calculateRateForDate(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        try {
            List<CurrencyData> lastMonthData = getLastMonthData(currencyDataList, targetDate);
            int n = lastMonthData.size();
            double[] x = new double[n];
            double[] y = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = lastMonthData.get(i).getDate().getDayOfMonth();
                y[i] = lastMonthData.get(i).getRate().doubleValue();
            }
            LinearRegression linearRegression = new LinearRegression(x, y);

            double predictedRate = linearRegression.predict(targetDate.getDayOfMonth());

            return new CurrencyData(targetDate, BigDecimal.valueOf(predictedRate));
        } catch (RuntimeException e) {
            logger.error("Failed to calculate the rate for the specified date: {}", e.getMessage(), e);
            throw new InvalidPredictionDataException("Failed to calculate the rate for the specified date");
        }
    }

    /**
     * Retrieves the currency data for the last month before the target date.
     * If the target date is not found in the data, the method returns data for the last 30 days.
     *
     * @param currencyDataList The list of historical currency data.
     * @param targetDate       The target date for which the data is retrieved.
     * @return A list of currency data for the last month before the target date or the last 30 days if the target date is not found.
     */
    public List<CurrencyData> getLastMonthData(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        boolean targetDateExists = currencyDataList.stream()
                .anyMatch(data -> data.getDate().isEqual(targetDate));

        if (targetDateExists) {
            LocalDate lastMonthDate = DateUtils.getLastMonthDate(targetDate);
            return currencyDataList.stream()
                    .filter(data -> data.getDate().isAfter(lastMonthDate))
                    .collect(Collectors.toList());
        } else {
            return currencyDataList.stream()
                    .sorted(Comparator.comparing(CurrencyData::getDate).reversed())
                    .limit(DAYS_IN_MONTH)
                    .collect(Collectors.toList());
        }
    }
}