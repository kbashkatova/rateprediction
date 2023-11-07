package ru.liga.rateforecaster.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.formatter.outputgenerator.ChartOutputGenerator;
import ru.liga.rateforecaster.formatter.outputgenerator.OutputGenerator;
import ru.liga.rateforecaster.formatter.outputgenerator.StringOutputGenerator;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Implementation of the {@link ResultFormatter} interface for formatting and generating results.
 */
public class ResultFormatterImpl implements ResultFormatter {
    private static final Logger logger = LoggerFactory.getLogger(ResultFormatterImpl.class);
    private final OutputGenerator outputGenerator;

    public ResultFormatterImpl(ResourceBundle resourceBundle, ParsedRequest parsedRequest) {
        if (parsedRequest.outputType() == OutputType.LIST) {
            this.outputGenerator = new StringOutputGenerator(resourceBundle);
        } else if (parsedRequest.outputType() == OutputType.GRAPH) {
            this.outputGenerator = new ChartOutputGenerator();
        } else {
            logger.error("Invalid output type: " + parsedRequest.outputType());
            throw new IllegalArgumentException("Invalid output type: " + parsedRequest.outputType());
        }
    }

    /**
     * Format the given data based on the output type.
     *
     * @param currencyDataForResultOutput List of data to be formatted
     * @param parsedRequest The parsed request containing output type and rate type
     * @return A {@link FormattedResult} object containing the formatted result
     */
    @Override
    public FormattedResult format(List<CurrencyDataForResultOutput> currencyDataForResultOutput, ParsedRequest parsedRequest) {
            return outputGenerator.format(currencyDataForResultOutput, parsedRequest);
    }
}