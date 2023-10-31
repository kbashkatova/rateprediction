package ru.liga.rateforecaster.forecast.generator;


import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.forecast.algorithm.factory.GenericPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.formatter.ResultFormatter;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The DateCurrencyForecastGenerator class is responsible for generating currency forecasts
 * for a specific date (day) based on user requests.
 * It extends the CurrencyForecastGenerator and provides specific logic for day forecasts.
 */
public class DateCurrencyForecastGenerator extends CurrencyForecastGenerator {

    private final ResultFormatter resultFormatter;

    public DateCurrencyForecastGenerator(Logger logger, ResultFormatter resultFormatter) {
        super(logger);
        this.resultFormatter = resultFormatter;
    }

    /**
     * Generates currency forecasts for the specified date (day) based on the parsed user request.
     *
     * @param parsedRequest The parsed user request.
     * @return A FormattedResult containing the formatted forecast or an error message.
     * @throws CsvValidationException If a CSV validation error occurs.
     * @throws IOException            If an IO error occurs.
     */
    @Override
    public FormattedResult generateForecast(ParsedRequest parsedRequest) throws CsvValidationException, IOException {
            final List<CurrencyDataForResultOutput> currencyDataForResultOutputs = new ArrayList<>();
            for (Currency currency : parsedRequest.getCurrencies()) {
                final LinkedList<CurrencyData> currencyDataList = createDataProcessor(currency).readCurrencyDataFromResources();
                final CurrencyData forecastCurs = calculateForecastForDate(currencyDataList, parsedRequest);
                currencyDataForResultOutputs.add(new CurrencyDataForResultOutput(currency,
                        new ArrayList<>(List.of(forecastCurs))));
            }
            return resultFormatter.format(currencyDataForResultOutputs, parsedRequest);
    }

    /**
     * Calculates the currency forecast for the specified date.
     *
     * @param currencyData   The currency data for the specific currency.
     * @param parsedRequest  The parsed user request.
     * @return The calculated CurrencyData representing the currency forecast.
     * @throws ArithmeticException If an arithmetic exception occurs during the calculation.
     */
    protected CurrencyData calculateForecastForDate(LinkedList<CurrencyData> currencyData,
                                              ParsedRequest parsedRequest) throws ArithmeticException {
        LocalDate targetDate = parsedRequest.getDate().orElseThrow(IllegalArgumentException::new);
        RatePredictionAlgorithm algorithm = GenericPredictionAlgorithm.createAlgorithm(parsedRequest);
        CurrencyData foreCastForDate = algorithm
                .getRateForDate(currencyData,
                        targetDate);
        if (foreCastForDate != null) {
            return foreCastForDate;
        } else {
            return algorithm.calculateRateForDate(currencyData, targetDate);
        }
    }

}
