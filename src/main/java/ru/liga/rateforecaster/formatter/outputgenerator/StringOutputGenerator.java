package ru.liga.rateforecaster.formatter.outputgenerator;

import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;

import java.util.List;

/**
 * An interface for generating formatted string output based on the provided data.
 */
public interface StringOutputGenerator {

    /**
     * Creates a formatted string output based on the provided data.
     *
     * @param currencyDataForResultOutput List of data for generating the string output
     * @param rateType                   Type of time interval (week or month)
     * @return The formatted string output
     */
    String createStringOutput(List<CurrencyDataForResultOutput> currencyDataForResultOutput, RateType rateType);

}
