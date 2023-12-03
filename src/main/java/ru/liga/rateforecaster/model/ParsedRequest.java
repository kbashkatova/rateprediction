package ru.liga.rateforecaster.model;

import lombok.Builder;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.ForecastingAlgorithm;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A class representing a parsed user request for a currency forecast.
 */
@Builder
public record ParsedRequest(List<Currency> currencies, Optional<LocalDate> date, RateType rateType, ForecastingAlgorithm algorithm,
                            OutputType outputType) {
}
