package ru.liga.rateforecaster.forecast.generator;


import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.data.factory.CurrencyPathResolver;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.formatter.ResultFormatter;
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
    private static final Logger logger = LoggerFactory.getLogger(CurrencyForecastGenerator.class);

    private final ResultFormatter resultFormatter;
    private final RatePredictionAlgorithm ratePredictionAlgorithm;
    private static final int NUMBER_OF_DAYS_IN_A_WEEK = 7;

    public WeeklyCurrencyForecastGenerator(ResultFormatter resultFormatter,
                                           RatePredictionAlgorithm ratePredictionAlgorithm,
                                           CurrencyPathResolver currencyPathResolver) {
        super(currencyPathResolver);
        this.resultFormatter = resultFormatter;
        this.ratePredictionAlgorithm = ratePredictionAlgorithm;
    }

    /**
     * Generates weekly currency forecasts based on the parsed request and data from resources.
     *
     * @param parsedRequest The parsed user request.
     * @return A formatted result containing the weekly currency forecasts.
     * @throws CsvValidationException If there is an issue with CSV validation.
     * @throws IOException            If an I/O error occurs.
     */
    @Override
    public FormattedResult generateForecast(ParsedRequest parsedRequest) throws CsvValidationException, IOException {
        final LocalDate forecastPeriodEnd = DateUtils.getLastDayOfWeekForecast(parsedRequest.date().orElseGet(DateUtils::getCurrentDate));
        final List<CurrencyDataForResultOutput> currencyDataForResultOutputs = new ArrayList<>();
        for (Currency currency : parsedRequest.currencies()) {
            final LinkedList<CurrencyData> currencyDataList = createDataProcessor(currency).readCurrencyDataFromResources();
            List<CurrencyData> forecastData = calculateWeeklyForecast(currencyDataList, forecastPeriodEnd);
            currencyDataForResultOutputs.add(new CurrencyDataForResultOutput(currency, forecastData));
        }

        return resultFormatter.format(currencyDataForResultOutputs, parsedRequest);
    }

    /**
     * Calculates a weekly currency forecast based on the currency data list and the target date.
     *
     * @param currencyDataList  The list of currency data for which the forecast is calculated.
     * @param forecastPeriodEnd The target date for the forecast.
     * @return A list of currency data representing the weekly forecast.
     */
    private List<CurrencyData> calculateWeeklyForecast(LinkedList<CurrencyData> currencyDataList, LocalDate forecastPeriodEnd) {
        final CurrencyData rateForDate = ratePredictionAlgorithm.getRateForDate(currencyDataList, forecastPeriodEnd);

        if (rateForDate == null) {
            List<CurrencyData> resultData = new ArrayList<>();
            List<LocalDate> targetDates = new ArrayList<>();
            LocalDate currentDate = forecastPeriodEnd;
            for (int i = 0; i < NUMBER_OF_DAYS_IN_A_WEEK; i++) {
                targetDates.add(currentDate);
                currentDate = DateUtils.getPreviousDate(currentDate);
            }
            for (LocalDate date : targetDates) {
                resultData.add(ratePredictionAlgorithm.calculateRateForDate(currencyDataList, date));
            }
            logger.info("Calculated weekly forecast for {} days", NUMBER_OF_DAYS_IN_A_WEEK);
            return resultData;
        }
        logger.info("Using available data for weekly forecast");
        return currencyDataList.subList(0, NUMBER_OF_DAYS_IN_A_WEEK);
    }
}