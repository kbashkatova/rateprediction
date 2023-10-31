package ru.liga.rateforecaster.formatter;

import org.jfree.chart.ChartPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.outputgenerator.ChartOutputGenerator;
import ru.liga.rateforecaster.formatter.outputgenerator.ChartOutputGeneratorImpl;
import ru.liga.rateforecaster.formatter.outputgenerator.StringOutputGenerator;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.formatter.outputgenerator.StringOutputGeneratorImpl;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Implementation of the {@link ResultFormatter} interface for formatting and generating results.
 */
/**
 * Implementation of the {@link ResultFormatter} interface for formatting and generating results.
 */
public class ResultFormatterImpl implements ResultFormatter {
    private StringOutputGenerator stringOutputGenerator;
    private ChartOutputGenerator chartOutputGenerator;
    private static final Logger logger = LoggerFactory.getLogger(ResultFormatterImpl.class);

    public ResultFormatterImpl(ResourceBundle resourceBundle, ParsedRequest parsedRequest) {
        if (parsedRequest.getOutputType() == OutputType.LIST) {
            this.stringOutputGenerator = new StringOutputGeneratorImpl(resourceBundle);
        } else if (parsedRequest.getOutputType() == OutputType.GRAPH) {
            this.chartOutputGenerator = new ChartOutputGeneratorImpl(resourceBundle);
        } else {
            logger.error("Invalid output type: " + parsedRequest.getOutputType());
            throw new IllegalArgumentException("Invalid output type: " + parsedRequest.getOutputType());
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
        switch (parsedRequest.getOutputType()) {
            case LIST -> {
                return new FormattedResult(generateListOutput(currencyDataForResultOutput, parsedRequest.getRateType()));
            }
            case GRAPH -> {
                return new FormattedResult(generateGraphOutput(currencyDataForResultOutput, parsedRequest.getRateType()));
            }
            default -> {
                logger.error("Invalid output type: " + parsedRequest.getOutputType());
                throw new IllegalArgumentException("Invalid output type: " + parsedRequest.getOutputType());
            }
        }
    }

    private String generateListOutput(List<CurrencyDataForResultOutput> currencyDataForResultOutput, RateType rateType) {
        return stringOutputGenerator.createStringOutput(currencyDataForResultOutput, rateType);
    }

    private ChartPanel generateGraphOutput(List<CurrencyDataForResultOutput> currencyDataForResultOutput, RateType rateType) {
        return chartOutputGenerator.createChart(currencyDataForResultOutput, rateType);
    }
}