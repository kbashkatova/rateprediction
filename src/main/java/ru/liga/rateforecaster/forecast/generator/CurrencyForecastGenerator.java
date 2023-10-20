package ru.liga.rateforecaster.forecast.generator;

import com.opencsv.exceptions.CsvValidationException;
import ru.liga.rateforecaster.data.factory.CurrencyProcessorFactory;
import ru.liga.rateforecaster.data.factory.GenericCurrencyProcessorFactory;
import ru.liga.rateforecaster.forecast.algorithm.RateCalculator;
import ru.liga.rateforecaster.model.Currency;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.model.RateType;
import ru.liga.rateforecaster.data.processor.CurrencyDataProcessor;
import ru.liga.rateforecaster.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * The CurrencyForecastGenerator class represents an abstract generator for currency forecasts.
 * It provides methods to create forecasts and fill in missing data in currency data lists.
 * Subclasses of this class implement specific forecast generation logic.
 */
public abstract class CurrencyForecastGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyForecastGenerator.class);

    public abstract String generateForecast(Currency currency) throws IOException, ParseException, CsvValidationException;

    /**
     * Создает экземпляр CurrencyForecastGenerator в зависимости от типа прогноза.
     *
     * @param forecastType Тип прогноза (например, RateType.TOMORROW или RateType.WEEK).
     * @return Экземпляр CurrencyForecastGenerator для указанного типа прогноза.
     */
    public static CurrencyForecastGenerator selectForecastGenerator(RateType forecastType) {

        if (forecastType.equals(RateType.TOMORROW)) {
            return new TomorrowCurrencyForecastGenerator();
        } else if (forecastType.equals(RateType.WEEK)) {
            return new WeeklyCurrencyForecastGenerator();
        } else {
            throw new IllegalArgumentException("Invalid forecast type: " + forecastType);
        }
    }

    /**
     * * Fills in the missing dates in the given list of currency data from the current date up to the target date.
     *
     * @param currencyDataList The list of currency data read form resources to update with missing dates and average rates.
     * @param currentDate      The current date from which to start filling in missing dates.
     * @param targetDate       The target date up to which the missing dates should be filled.
     * @return A list of currency data that includes the missing dates and their average rates.
     * @throws RuntimeException if an error occurs while filling in the missing dates.
     */
    protected List<CurrencyData> fillMissingDates(LinkedList<CurrencyData> currencyDataList, LocalDate currentDate, LocalDate targetDate) {
        try {
            if (currentDate.isEqual(targetDate.plusDays(1))) {
                return currencyDataList;
            } else {
                final BigDecimal averageCurs = RateCalculator.calculateAverageRateFromLastSevenRates(currencyDataList, currentDate);
                currencyDataList.addFirst(new CurrencyData(currentDate, averageCurs));
                final LocalDate nextDate = DateUtils.getNextDate(currentDate);
                final List<CurrencyData> newCurrencyData = fillMissingDates(currencyDataList, nextDate, targetDate);
                final List<CurrencyData> result = new ArrayList<>();
                result.add(new CurrencyData(currentDate, averageCurs));
                result.addAll(newCurrencyData);
                return result;
            }
        } catch (Exception e) {
            logger.error("Failed to fill missing dates: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fill missing dates", e);
        }
    }

    /**
     * Creates a data processor for the specified currency.
     *
     * @param currency The currency for which the data processor should be created.
     * @return A data processor for the specified currency.
     * @throws IllegalArgumentException if an invalid currency type is provided.
     * @throws RuntimeException if an error occurs while creating the data processor.
     */
    protected CurrencyDataProcessor createDataProcessor(Currency currency) {
        try {
            final CurrencyProcessorFactory factory = GenericCurrencyProcessorFactory.getFactory(currency);
            if (isNull(factory)) {
                throw new IllegalArgumentException("Invalid currency type: " + currency);
            }
            return factory.createCurrencyDataProcessor();
        } catch (RuntimeException e) {
            logger.error("Failed to create data processor: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create data processor", e);
        }
    }
}
