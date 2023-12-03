package ru.liga.rateforecaster.formatter;

import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.util.List;

/**
 * Interface for formatting the results of currency forecasting.
 */
public interface ResultFormatter {

    FormattedResult format(List<CurrencyDataForResultOutput> currency, ParsedRequest parsedRequest);
}
