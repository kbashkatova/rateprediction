package ru.liga.rateforecaster.forecast.generator;


import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import ru.liga.rateforecaster.forecast.algorithm.factory.GenericPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.formatter.ResultFormatter;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The WeeklyCurrencyForecastGenerator class represents a generator for weekly currency forecasts.
 * It provides methods to calculate and generate weekly forecasts based on the given data.
 */
public class WeeklyCurrencyForecastGenerator extends CurrencyForecastGenerator {

    private final ResultFormatter resultFormatter;

    public WeeklyCurrencyForecastGenerator(Logger logger, ResultFormatter resultFormatter) {
        super(logger);
        this.resultFormatter = resultFormatter;
    }

    /**
     * Generates weekly currency forecasts based on the parsed request and data from resources.
     *
     * @param parsedRequest The parsed user request.
     * @return A formatted result containing the weekly currency forecasts.
     * @throws CsvValidationException If there is an issue with CSV validation.
     * @throws IOException           If an I/O error occurs.
     */
    @Override
    public FormattedResult generateForecast(ParsedRequest parsedRequest) throws CsvValidationException, IOException {
        final LocalDate forecastPeriodEnd = DateUtils.getLastDayOfWeekForecast(
                parsedRequest.getDate().orElseGet(DateUtils::getCurrentDate));
        final List<CurrencyDataForResultOutput> currencyDataForResultOutputs = new ArrayList<>();
        for (Currency currency :
                parsedRequest.getCurrencies()) {
            final LinkedList<CurrencyData> currencyDataList = createDataProcessor(currency).readCurrencyDataFromResources();
            List<CurrencyData> forecastData = calculateWeeklyForecast(parsedRequest, currencyDataList, forecastPeriodEnd);
            currencyDataForResultOutputs.add(new CurrencyDataForResultOutput(currency, forecastData));
        }

        return resultFormatter.format(currencyDataForResultOutputs, parsedRequest);
    }

    /**
     * Calculates a weekly currency forecast based on the currency data list and the target date.
     *
     * @param parsedRequest     The parsed user request.
     * @param currencyDataList  The list of currency data for which the forecast is calculated.
     * @param forecastPeriodEnd The target date for the forecast.
     * @return A list of currency data representing the weekly forecast.
     */
    protected List<CurrencyData> calculateWeeklyForecast(ParsedRequest parsedRequest, LinkedList<CurrencyData> currencyDataList,
                                                         LocalDate forecastPeriodEnd) {
        RatePredictionAlgorithm algorithm = GenericPredictionAlgorithm.createAlgorithm(parsedRequest);
        final CurrencyData rateForDate = algorithm.getRateForDate(currencyDataList, forecastPeriodEnd);

        if (rateForDate == null) {
            List<CurrencyData> resultData = new ArrayList<>();
            List<LocalDate> targetDates = new ArrayList<>();
            LocalDate currentDate = forecastPeriodEnd;
            for (int i = 0; i < 7; i++) {
                targetDates.add(currentDate);
                currentDate = DateUtils.getPreviousDate(currentDate);
            }
            for (LocalDate date :
                    targetDates) {
                resultData.add(algorithm.calculateRateForDate(currencyDataList, date));
            }
            return resultData;
        }
        return currencyDataList.subList(0, 7);
    }
}