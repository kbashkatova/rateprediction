package ru.liga.rateforecaster.forecast.generator;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.data.factory.GenericCurrencyProcessorFactory;
import ru.liga.rateforecaster.data.processor.CurrencyDataProcessor;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.io.IOException;
import java.text.ParseException;

/**
 * The CurrencyForecastGenerator class represents an abstract generator for currency forecasts.
 * It provides methods to create forecasts and fill in missing data in currency data lists.
 * Subclasses of this class implement specific forecast generation logic.
 */
public abstract class CurrencyForecastGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyForecastGenerator.class);


    /**
     * Generates a forecast based on the provided parsed request.
     *
     * @param parsedRequest The parsed user request.
     * @return A FormattedResult containing the forecast or an error message.
     * @throws IOException            If an IO error occurs.
     * @throws ParseException         If a parsing error occurs.
     * @throws CsvValidationException If a CSV validation error occurs.
     */
    public abstract FormattedResult generateForecast(ParsedRequest parsedRequest) throws IOException,
            ParseException,
            CsvValidationException;

    /**
     * Creates a data processor for the specified currency.
     *
     * @param currency The currency for which to create the data processor.
     * @return A CurrencyDataProcessor for the specified currency.
     */
    protected CurrencyDataProcessor createDataProcessor(Currency currency) {
        try {
            return GenericCurrencyProcessorFactory.getFactory(currency).createCurrencyDataProcessor();
        } catch (RuntimeException e) {
            logger.error("Failed to create data processor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create data processor", e);
        }
    }
}
