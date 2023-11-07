package ru.liga.rateforecaster.model;

import lombok.Builder;
import lombok.Getter;
import ru.liga.rateforecaster.enums.*;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;

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
