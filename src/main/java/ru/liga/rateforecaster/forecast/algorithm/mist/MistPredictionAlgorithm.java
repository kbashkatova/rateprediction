package ru.liga.rateforecaster.forecast.algorithm.mist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.exception.InvalidPredictionDataException;
import ru.liga.rateforecaster.forecast.algorithm.average.AveragePredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * The MistPredictionAlgorithm class represents a prediction algorithm that uses a randomized approach.
 * It generates forecasts based on random data from the provided currency data list.
 */
public class MistPredictionAlgorithm extends RatePredictionAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(AveragePredictionAlgorithm.class);

    private final RandomNumberGenerator randomNumberGenerator;

    /**
     * Creates a MistPredictionAlgorithm with a default random number generator.
     */
    public MistPredictionAlgorithm() {
        this.randomNumberGenerator = new DefaultRandomNumberGenerator();
    }

    /**
     * Creates a MistPredictionAlgorithm with a custom random number generator.
     *
     * @param randomNumberGenerator The custom random number generator to use.
     */
    public MistPredictionAlgorithm(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    /**
     * Calculates the currency rate for the specified target date using the Mist prediction algorithm.
     *
     * @param currencyDataList The list of currency data to use for prediction.
     * @param targetDate       The target date for the prediction.
     * @return The calculated CurrencyData for the target date.
     */
    @Override
    public CurrencyData calculateRateForDate(List<CurrencyData> currencyDataList, LocalDate targetDate) {
        try {
            Optional<CurrencyData> currentYearRate = findTargetDateRate(currencyDataList, targetDate);
            if (currentYearRate.isPresent()) {
                return currentYearRate.get();
            }

            int randomIndex = randomNumberGenerator.nextInt(currencyDataList.size());

            return currencyDataList.get(randomIndex);
        } catch (RuntimeException e) {
            logger.error("Failed to calculate the rate for the specified date: " + e.getMessage(), e);
            throw new InvalidPredictionDataException("Failed to calculate the rate for the specified date");
        }
    }

    private Optional<CurrencyData> findTargetDateRate(List<CurrencyData> currencyDataList, LocalDate date) {
        return currencyDataList.stream()
                .filter(data -> data.date().isEqual(date))
                .findFirst();
    }
}