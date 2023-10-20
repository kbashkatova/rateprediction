package ru.liga.rateforecaster.forecast.generator;


import com.opencsv.exceptions.CsvValidationException;
import ru.liga.rateforecaster.forecast.algorithm.RateCalculator;
import ru.liga.rateforecaster.formatter.ResultFormatter;
import ru.liga.rateforecaster.model.Currency;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TomorrowCurrencyForecastGenerator extends CurrencyForecastGenerator implements ResultFormatter {

    /**
     * Generates a currency forecast for the specified currency for the next day.
     *
     * @param currency The currency for which the forecast should be generated.
     * @return A string representing the currency forecast for the next day.
     * @throws CsvValidationException if an error occurs during CSV validation.
     * @throws IOException if an error occurs while reading data from resources.
     */
    @Override
    public String generateForecast(Currency currency) throws CsvValidationException, IOException {
        final LocalDate forecastDate = DateUtils.getTomorrowDate();
        final LinkedList<CurrencyData> currencyDataList =  createDataProcessor(currency).readCurrencyDataFromResources();
        final BigDecimal forecastCurs = calculateForecastForDate(currencyDataList, forecastDate);
        return format(currency, new ArrayList<>(List.of(new CurrencyData(forecastDate, forecastCurs))));
    }

    /**
     * Calculates the forecasted currency rate for a specific date based on the given currency data list and target date.
     *
     * @param currencyData The list of currency data used for forecasting.
     * @param targetDate The target date for which the forecast is calculated.
     * @return The forecasted currency rate for the target date.
     * @throws ArithmeticException if the forecast cannot be calculated.
     */
    protected BigDecimal calculateForecastForDate(LinkedList<CurrencyData> currencyData,
                                              LocalDate targetDate) throws ArithmeticException {
        CurrencyData rateForDate = RateCalculator.calculateRateForDate(currencyData, targetDate);
        if (rateForDate != null) {
            return rateForDate.getRate();
        } else {
            fillMissingDates(currencyData, currencyData.get(0).getDate(), targetDate);
            rateForDate = RateCalculator.calculateRateForDate(currencyData, targetDate);

            if (rateForDate != null) {
                return rateForDate.getRate();
            } else {
                throw new ArithmeticException();
            }
        }
    }

    /**
     * Formats the tomorrow's currency forecast data for display.
     *
     * @param currency      The currency for which the forecast is made.
     * @param forecastData  A list of currency data representing the tomorrow's forecast.
     * @return A formatted string representing the tomorrow's forecast data.
     */
    @Override
    public String format(Currency currency, List<CurrencyData> forecastData) {
        if (!forecastData.isEmpty()) {
            return String.format("rate %s tomorrow %s - %.2f;", currency, forecastData.get(0).getDate(),
                    forecastData.get(0).getRate());
        }
        return "No forecast data available.";
    }
}
