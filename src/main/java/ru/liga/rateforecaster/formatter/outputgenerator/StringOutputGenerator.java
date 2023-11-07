package ru.liga.rateforecaster.formatter.outputgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.utils.DateUtils;

import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

/**
 * An implementation of the {@link OutputGenerator} interface for generating formatted string output based on the provided data.
 */
public class StringOutputGenerator implements OutputGenerator {

    private static final Logger logger = LoggerFactory.getLogger(StringOutputGenerator.class);
    private final ResourceBundle resourceBundle;

    public StringOutputGenerator(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    /**
     * Format the data into a string based on the provided rate type and currency data.
     *
     * @param currencyDataForResultOutput List of data to be formatted.
     * @param parsedRequest               The parsed request containing the rate type.
     * @return A {@link FormattedResult} object containing the formatted string.
     */
    @Override
    public FormattedResult format(List<CurrencyDataForResultOutput> currencyDataForResultOutput, ParsedRequest parsedRequest) {
        if (currencyDataForResultOutput.isEmpty()) {
            return generateErrorOutput();
        }

        for (CurrencyDataForResultOutput dataForResultOutput : currencyDataForResultOutput) {
            switch (parsedRequest.rateType()) {
                case DAY:
                    return generateDayOutput(dataForResultOutput.getCurrency(), dataForResultOutput.getForecastData());
                case WEEK:
                    return generateWeekOutput(dataForResultOutput.getCurrency(), dataForResultOutput.getForecastData());
                case MONTH:
                    return generateMonthOutput(dataForResultOutput.getCurrency(), dataForResultOutput.getForecastData());
                default:
                    logger.error("Invalid RateType: " + parsedRequest.rateType());
                    return generateErrorOutput();
                }
            }
        return generateErrorOutput();
    }

    private FormattedResult generateDayOutput(Currency currency, List<CurrencyData> forecastData) {
        if (forecastData.size() == 1) {
            return new FormattedResult(String.format("rate %s tomorrow %s - %.2f;", currency,
                    DateUtils.getFormattedDateForOutput(forecastData.get(0).date()),
                    forecastData.get(0).rate()));
        }
        logger.error("Invalid forecast data for generating day output.");
        return generateErrorOutput();
    }

    private FormattedResult generateWeekOutput(Currency currency, List<CurrencyData> forecastData) {
        final StringBuilder formattedResult = new StringBuilder();
        formattedResult.append(String.format("rate %s week%n", currency));
        formattedResult.append(generateListOfData(forecastData));
        return new FormattedResult(formattedResult.toString());
    }

    private FormattedResult generateMonthOutput(Currency currency, List<CurrencyData> forecastData) {
        final StringBuilder formattedResult = new StringBuilder();
        formattedResult.append(String.format("rate %s month%n", currency));
        formattedResult.append(generateListOfData(forecastData));
        return new FormattedResult(formattedResult.toString());
    }

    private String generateListOfData(List<CurrencyData> forecastData) {
        StringBuilder formattedResult = new StringBuilder();
        final ListIterator<CurrencyData> iterator = forecastData.listIterator(forecastData.size());
        while (iterator.hasPrevious()) {
            final CurrencyData data = iterator.previous();
            formattedResult.append(String.format("%s - %.2f%n",
                    DateUtils.getFormattedDateForOutput(data.date()),
                    data.rate()));
        }
        return formattedResult.toString();
    }

    private FormattedResult generateErrorOutput() {
        logger.error("No output data available.");
        return new FormattedResult(resourceBundle.getString("no_output_data_available"));
    }
}
