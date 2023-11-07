package ru.liga.rateforecaster.forecast.generator.factory;

import ru.liga.rateforecaster.forecast.generator.CurrencyForecastGenerator;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.util.ResourceBundle;

public interface CurrencyForecastGeneratorFactory {
    CurrencyForecastGenerator createGenerator(ParsedRequest parsedRequest, ResourceBundle resourceBundle);
}
