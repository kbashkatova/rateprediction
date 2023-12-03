package ru.liga.rateforecaster.forecast;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.data.pathresolver.CurrencyPathResolver;
import ru.liga.rateforecaster.forecast.algorithm.factory.GenericPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.generator.CurrencyForecastGenerator;
import ru.liga.rateforecaster.forecast.generator.factory.CurrencyForecastGeneratorFactoryImpl;
import ru.liga.rateforecaster.model.ErrorMessage;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.telegrambot.model.BotState;

import java.io.IOException;
import java.text.ParseException;
import java.util.ResourceBundle;

/**
 * UserRequestForecastGenerator is responsible for processing user requests,
 * parsing them, generating forecasts, and handling errors.
 */
public class UserRequestForecastGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UserRequestForecastGenerator.class);
    private final ResourceBundle resourceBundle;

    private final GenericPredictionAlgorithm genericPredictionAlgorithm;
    private final CurrencyPathResolver currencyPathResolver;

    public UserRequestForecastGenerator(ResourceBundle resourceBundle,
                                        GenericPredictionAlgorithm genericPredictionAlgorithm,
                                        CurrencyPathResolver currencyPathResolver) {
        this.resourceBundle = resourceBundle;
        this.genericPredictionAlgorithm = genericPredictionAlgorithm;
        this.currencyPathResolver = currencyPathResolver;
    }
    /**
     * Proceeds with the user's request, generates a forecast, and returns a FormattedResult.
     *
     * @param botState The state of the user's request as a BotState object.
     * @return A FormattedResult containing the forecast or an error message.
     */
    public FormattedResult proceedUserRequest(BotState botState) {
        return generateForecast(ParsedRequest.builder()
                .currencies(botState.getCurrencies())
                .date(botState.getDate())
                .rateType(botState.getPeriod())
                .algorithm(botState.getAlgorithm())
                .outputType(botState.getOutputType())
                .build());
    }

    /**
     * Generates a forecast based on the user's parsed request.
     *
     * @param parsedRequest The parsed user request.
     * @return A FormattedResult containing the forecast or an error message.
     */
    private FormattedResult generateForecast(ParsedRequest parsedRequest) {
        try {
            final CurrencyForecastGenerator forecast = new CurrencyForecastGeneratorFactoryImpl(
                    genericPredictionAlgorithm, currencyPathResolver).createGenerator(parsedRequest, resourceBundle);
            return forecast.generateForecast(parsedRequest);
        } catch (CsvValidationException e) {
            logger.error("CSV validation error: ", e.getMessage());
            return new FormattedResult(new ErrorMessage(resourceBundle.getString("csvValidationError")));
        } catch (ParseException e) {
            logger.error("Parse error: " + e.getMessage(), e);
            return new FormattedResult(new ErrorMessage(resourceBundle.getString("parseError")));
        } catch (IOException e) {
            logger.error("IO error: " + e.getMessage(), e);
            return new FormattedResult(new ErrorMessage(resourceBundle.getString("ioError")));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: " + e.getMessage(), e);
            return new FormattedResult(new ErrorMessage(resourceBundle.getString("invalidRequestError")));
        } catch (Exception e) {
            logger.error("Failed to generate forecast: " + e.getMessage(), e);
            return new FormattedResult(new ErrorMessage(resourceBundle.getString("genericError")));
        }
    }
}