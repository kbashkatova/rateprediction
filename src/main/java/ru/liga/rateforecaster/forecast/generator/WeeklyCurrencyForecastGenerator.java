package ru.liga.rateforecaster.forecast.generator;


import com.opencsv.exceptions.CsvValidationException;
import ru.liga.rateforecaster.forecast.algorithm.RateCalculator;
import ru.liga.rateforecaster.formatter.ResultFormatter;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class WeeklyCurrencyForecastGenerator extends CurrencyForecastGenerator implements ResultFormatter {
    /**
     * Generates a weekly currency forecast for the specified currency.
     *
     * @param currency The currency for which the weekly forecast should be generated.
     * @return A string representing the weekly currency forecast.
     * @throws CsvValidationException if an error occurs during CSV validation.
     * @throws IOException if an error occurs while reading data from resources.
     */
    @Override
    public String generateForecast(Currency currency) throws CsvValidationException, IOException {
        final LocalDate forecastStartEnd = DateUtils.getLastDayOfWeekForecast();
        final LinkedList<CurrencyData> currencyDataList = createDataProcessor(currency).readCurrencyDataFromResources();
        return format(currency, calculateWeeklyForecast(currencyDataList, forecastStartEnd));
    }

    /**
     * Calculates a weekly currency forecast based on the currency data list and the target date.
     *
     * @param currencyDataList The list of currency data for which the forecast is calculated.
     * @param targetDate The target date for the forecast.
     * @return A list of currency data representing the weekly forecast.
     */
    protected List<CurrencyData> calculateWeeklyForecast(LinkedList<CurrencyData> currencyDataList,
                                                         LocalDate targetDate) {
        final CurrencyData rateForDate = RateCalculator.calculateRateForDate(currencyDataList, targetDate);

        if (rateForDate == null) {
            fillMissingDates(currencyDataList, currencyDataList.get(0).getDate(), targetDate);
        }
        return currencyDataList.subList(0, 7);
    }

    /**
     * Formats the weekly currency forecast data for display.
     *
     * @param currency      The currency for which the forecast is made.
     * @param forecastData  A list of currency data including the weekly forecast.
     * @return A formatted string representing the weekly forecast data.
     */
    @Override
    public String format(Currency currency, List<CurrencyData> forecastData) {
        final StringBuilder formattedResult = new StringBuilder();
        formattedResult.append(String.format("rate %s week%n", currency));

       final ListIterator<CurrencyData> iterator = forecastData.listIterator(forecastData.size());
        while (iterator.hasPrevious()) {
            final CurrencyData data = iterator.previous();
            formattedResult.append(String.format("%s - %.2f%n", data.getDate(), data.getRate()));
        }

        return formattedResult.toString();
    }
}