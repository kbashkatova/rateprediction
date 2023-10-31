package ru.liga.rateforecaster.forecast.algorithm.factory;

import ru.liga.rateforecaster.enums.Algorithm;
import ru.liga.rateforecaster.forecast.algorithm.*;
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
    public static RatePredictionAlgorithm createAlgorithm(ParsedRequest parsedRequest) {
    Algorithm algorithm = parsedRequest.getAlgorithm();

    switch (algorithm) {
        case AVERAGE:
            return new AveragePredictionAlgorithm();
        case YEAR:
            return new YearPredictionAlgorithm();
        case MIST:
            return new MistPredictionAlgorithm();
        case MOON:
            return new MoonPredictionAlgorithm();
        default:
            throw new IllegalArgumentException("Invalid algorithm: " + algorithm);
    }
}
}
