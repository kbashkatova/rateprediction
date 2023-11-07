package ru.liga.rateforecaster.formatter.outputgenerator;

import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.util.List;

public interface OutputGenerator {
    FormattedResult format(List<CurrencyDataForResultOutput> currencyDataForResultOutput, ParsedRequest parsedRequest);
}
