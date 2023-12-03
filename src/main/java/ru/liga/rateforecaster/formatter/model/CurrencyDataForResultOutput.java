package ru.liga.rateforecaster.formatter.model;

import lombok.Getter;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.model.CurrencyData;

import java.util.List;


/**
 * Represents the data for a specific currency that will be included in the forecast output.
 */
@Getter
public class CurrencyDataForResultOutput {

    private final Currency currency;
    private final List<CurrencyData> forecastData;

    public CurrencyDataForResultOutput(Currency currency, List<CurrencyData> forecastData) {
        this.currency = currency;
        this.forecastData = forecastData;
    }
}
