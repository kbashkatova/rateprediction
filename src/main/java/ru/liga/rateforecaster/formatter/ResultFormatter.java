package ru.liga.rateforecaster.formatter;

import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.model.CurrencyData;

import java.util.List;

/**
 * Interface for formatting the results of currency forecasting.
 */
public interface ResultFormatter {
    /**
     * Formats the forecasted currency data for display.
     *
     * @param currency      The currency for which the forecast is made.
     * @param forecastData  A list of currency data including the forecast data.
     * @return A formatted string representing the forecast data.
     */
    String format(Currency currency, List<CurrencyData> forecastData);
}
