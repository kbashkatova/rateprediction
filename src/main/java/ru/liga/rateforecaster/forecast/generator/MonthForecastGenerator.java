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
import ru.liga.rateforecaster.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The MonthForecastGenerator class is responsible for generating currency forecasts for a month
 * based on user requests. It extends the CurrencyForecastGenerator and provides specific logic for monthly forecasts.
 */
public class MonthForecastGenerator extends CurrencyForecastGenerator {

    private final ResultFormatter resultFormatter;
    private final static int DAYS_IN_MONTH_FORECAST = 30;

    public MonthForecastGenerator(Logger logger, ResultFormatter resultFormatter) {
        super(logger);
        this.resultFormatter = resultFormatter;
    }

    /**
     * Generates currency forecasts for a month based on the parsed user request.
     *
     * @param parsedRequest The parsed user request.
     * @return A FormattedResult containing the formatted forecast or an error message.
     * @throws CsvValidationException If a CSV validation error occurs.
     * @throws IOException            If an IO error occurs.
     */
    @Override
    public FormattedResult generateForecast(ParsedRequest parsedRequest) throws CsvValidationException, IOException {

        final LocalDate forecastStartEnd = DateUtils.getLastDayOfMonthForecast(
                parsedRequest.getDate().orElseGet(DateUtils::getCurrentDate));
        final List<CurrencyDataForResultOutput> currencyDataForResultOutputs = new ArrayList<>();
        for (Currency currency :
                parsedRequest.getCurrencies()) {
            final LinkedList<CurrencyData> currencyDataList = createDataProcessor(currency).readCurrencyDataFromResources();
            List<CurrencyData> forecastData = calculateMonthlyForecast(parsedRequest, currencyDataList, forecastStartEnd);
            currencyDataForResultOutputs.add(new CurrencyDataForResultOutput(currency, forecastData));
        }
        return resultFormatter.format(
                currencyDataForResultOutputs,
                parsedRequest);
    }

    /**
     * Calculates the currency forecast for a month.
     *
     * @param parsedRequest       The parsed user request.
     * @param currencyDataList    The list of currency data.
     * @param forecastPeriodEnd   The end date of the forecast period.
     * @return A list of CurrencyData representing the currency forecasts for the month.
     */
    protected List<CurrencyData> calculateMonthlyForecast(ParsedRequest parsedRequest, LinkedList<CurrencyData> currencyDataList,
                                                          LocalDate forecastPeriodEnd) {
        RatePredictionAlgorithm algorithm = GenericPredictionAlgorithm.createAlgorithm(parsedRequest);
        final CurrencyData rateForDate = algorithm
                .getRateForDate(currencyDataList, forecastPeriodEnd);

        if (rateForDate == null) {
            List<CurrencyData> resultData = new ArrayList<>();
            List<LocalDate> targetDates = new ArrayList<>();
            LocalDate currentDate = forecastPeriodEnd;
            for (int i = 0; i < DAYS_IN_MONTH_FORECAST; i++) {
                targetDates.add(currentDate);
                currentDate = DateUtils.getPreviousDate(currentDate);
            }
            for (LocalDate date:
                    targetDates) {
                resultData.add(algorithm.calculateRateForDate(currencyDataList, date));
            }
            return resultData;
        }
        return currencyDataList.subList(0, DAYS_IN_MONTH_FORECAST);
    }
}