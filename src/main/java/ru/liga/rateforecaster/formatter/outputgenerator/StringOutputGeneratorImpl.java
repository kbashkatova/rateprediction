package ru.liga.rateforecaster.formatter.outputgenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

/**
 * Implementation of the {@link StringOutputGenerator} interface for generating string-based formatted output.
 */
public class StringOutputGeneratorImpl extends BasicResultFormatter implements StringOutputGenerator {

    private static final Logger logger = LoggerFactory.getLogger(StringOutputGeneratorImpl.class);

    public StringOutputGeneratorImpl(ResourceBundle resourceBundle) {
        super(resourceBundle);
    }

    /**
     * Creates a formatted string output based on the provided data.
     *
     * @param currencyDataForResultOutput List of data for generating the string output
     * @param rateType                   Type of time interval (week or month)
     * @return The formatted string output
     */
    @Override
    public String createStringOutput(List<CurrencyDataForResultOutput> currencyDataForResultOutput, RateType rateType) {
        if (currencyDataForResultOutput.isEmpty()) {
            return generateErrorOutput();
        }

        StringBuilder resultOutput = new StringBuilder();
        for (CurrencyDataForResultOutput dataForResultOutput : currencyDataForResultOutput) {
            switch (rateType) {
                case DAY -> resultOutput.append(generateDayOutput(dataForResultOutput.getCurrency(), dataForResultOutput.getForecastData()));
                case WEEK -> resultOutput.append(generateWeekOutput(dataForResultOutput.getCurrency(), dataForResultOutput.getForecastData()));
                case MONTH -> resultOutput.append(generateMonthOutput(dataForResultOutput.getCurrency(), dataForResultOutput.getForecastData()));
                default -> {
                    logger.error("Invalid RateType: " + rateType);
                    return generateErrorOutput();
                }
            }
        }
        return resultOutput.toString();
    }

    public String generateDayOutput(Currency currency, List<CurrencyData> forecastData) {
        if (forecastData.size() == 1) {
            return String.format("rate %s tomorrow %s - %.2f;", currency,
                    DateUtils.getFormattedDateForOutput(forecastData.get(0).getDate()),
                    forecastData.get(0).getRate());
        }
        logger.error("Invalid forecast data for generating day output.");
        return generateErrorOutput();
    }

    public String generateWeekOutput(Currency currency, List<CurrencyData> forecastData) {
        final StringBuilder formattedResult = new StringBuilder();
        formattedResult.append(String.format("rate %s week%n", currency));
        formattedResult.append(generateListOfData(forecastData));
        return formattedResult.toString();
    }

    public String generateMonthOutput(Currency currency, List<CurrencyData> forecastData) {
        final StringBuilder formattedResult = new StringBuilder();
        formattedResult.append(String.format("rate %s month%n", currency));
        formattedResult.append(generateListOfData(forecastData));
        return formattedResult.toString();
    }

    private String generateListOfData(List<CurrencyData> forecastData) {
        StringBuilder formattedResult = new StringBuilder();
        final ListIterator<CurrencyData> iterator = forecastData.listIterator(forecastData.size());
        while (iterator.hasPrevious()) {
            final CurrencyData data = iterator.previous();
            formattedResult.append(String.format("%s - %.2f%n",
                    DateUtils.getFormattedDateForOutput(data.getDate()),
                    data.getRate()));
        }
        return formattedResult.toString();
    }

    private String generateErrorOutput() {
        logger.error("No output data available.");
        return resourceBundle.getString("no_output_data_available");
    }
}