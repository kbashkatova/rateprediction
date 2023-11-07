package ru.liga.rateforecaster.forecast.generator.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.factory.GenericPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.generator.CurrencyForecastGenerator;
import ru.liga.rateforecaster.forecast.generator.DateCurrencyForecastGenerator;
import ru.liga.rateforecaster.forecast.generator.MonthForecastGenerator;
import ru.liga.rateforecaster.forecast.generator.WeeklyCurrencyForecastGenerator;
import ru.liga.rateforecaster.formatter.ResultFormatter;
import ru.liga.rateforecaster.formatter.ResultFormatterImpl;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.util.ResourceBundle;

/**
 * The CurrencyForecastGeneratorFactoryImpl class implements the CurrencyForecastGeneratorFactory interface.
 * It is responsible for creating specific CurrencyForecastGenerator instances based on the provided ParsedRequest.
 */
public class CurrencyForecastGeneratorFactoryImpl implements CurrencyForecastGeneratorFactory {
    /**
     * Creates a specific CurrencyForecastGenerator instance based on the provided ParsedRequest.
     *
     * @param parsedRequest   The parsed user request.
     * @param resourceBundle The resource bundle for localization.
     * @return A CurrencyForecastGenerator for the specified forecast type.
     * @throws IllegalArgumentException If an invalid forecast type is specified in the ParsedRequest.
     */
    @Override
    public CurrencyForecastGenerator createGenerator(ParsedRequest parsedRequest, ResourceBundle resourceBundle) {
        RatePredictionAlgorithm ratePredictionAlgorithm = GenericPredictionAlgorithm.createAlgorithm(parsedRequest);
        ResultFormatter resultFormatter = initializeResourceFormatter(parsedRequest, resourceBundle);
        return switch (parsedRequest.rateType()) {
            case DAY -> new DateCurrencyForecastGenerator(resultFormatter, ratePredictionAlgorithm);
            case WEEK -> new WeeklyCurrencyForecastGenerator(resultFormatter, ratePredictionAlgorithm);
            case MONTH -> new MonthForecastGenerator(resultFormatter, ratePredictionAlgorithm);
            default -> throw new IllegalArgumentException("Invalid forecast type: " + parsedRequest.rateType());
        };
    }


    /**
     * Initializes the ResultFormatter with the provided ParsedRequest and ResourceBundle.
     *
     * @param parsedRequest   The parsed user request.
     * @param resourceBundle The resource bundle for localization.
     * @return A ResultFormatter instance for formatting results.
     */
    private ResultFormatter initializeResourceFormatter(ParsedRequest parsedRequest, ResourceBundle resourceBundle) {
        return new ResultFormatterImpl(resourceBundle, parsedRequest);
    }
}