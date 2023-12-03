package ru.liga.rateforecaster.forecast.algorithm.factory;

import ru.liga.rateforecaster.enums.ForecastingAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.average.AveragePredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.mist.MistPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.moon.MoonPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.year.YearPredictionAlgorithm;
import ru.liga.rateforecaster.model.ParsedRequest;

/**
 * The GenericPredictionAlgorithm class is a factory for creating rate prediction algorithms based on the specified algorithm type.
 * It provides a method to create a specific prediction algorithm according to the user's request.
 */
public class GenericPredictionAlgorithm {


    /**
     * Creates a rate prediction algorithm based on the specified parsed request.
     *
     * @param parsedRequest The parsed user request that specifies the algorithm type.
     * @return A rate prediction algorithm instance based on the user's request.
     * @throws IllegalArgumentException if the specified algorithm type is invalid.
     */
    public RatePredictionAlgorithm createAlgorithm(ParsedRequest parsedRequest) {
        ForecastingAlgorithm algorithm = parsedRequest.algorithm();

        return switch (algorithm) {
            case AVERAGE -> new AveragePredictionAlgorithm();
            case YEAR -> new YearPredictionAlgorithm();
            case MIST -> new MistPredictionAlgorithm();
            case MOON -> new MoonPredictionAlgorithm();
            default -> throw new IllegalArgumentException("Invalid algorithm: " + algorithm);
        };
    }
}
