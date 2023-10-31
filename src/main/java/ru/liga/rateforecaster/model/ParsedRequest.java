package ru.liga.rateforecaster.model;

import lombok.Builder;
import lombok.Getter;
import ru.liga.rateforecaster.enums.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * A class representing a parsed user request for a currency forecast.
 */
@Getter
@Builder
public class ParsedRequest {
    private final List<Currency> currencies;
    private final Optional<LocalDate> date;
    private final RateType rateType;
    private final Algorithm algorithm;
    private final OutputType outputType;


    public ParsedRequest(List<Currency> currencies, Optional<LocalDate> date, RateType rateType, Algorithm algorithm, OutputType outputType) {
        this.currencies = currencies;
        this.date = date;
        this.rateType = rateType;
        this.algorithm = algorithm;
        this.outputType = outputType;
    }

}
