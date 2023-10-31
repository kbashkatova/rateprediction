package ru.liga.rateforecaster.formatter.outputgenerator;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeriesCollection;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;

import java.util.List;

/**
 * An interface for generating charts and datasets for output.
 */
public interface ChartOutputGenerator {

    /**
     * Creates a chart for the given currency data and rate type.
     *
     * @param currencyDataForResultOutput The currency data for the output.
     * @param rateType The rate type for the chart.
     * @return A ChartPanel containing the generated chart.
     */
    ChartPanel createChart(List<CurrencyDataForResultOutput> currencyDataForResultOutput, RateType rateType);

    /**
     * Creates an XYSeriesCollection dataset for the given currency data and rate type.
     *
     * @param testData The currency data for the output.
     * @param rateType The rate type for the dataset.
     * @return An XYSeriesCollection dataset.
     */
    XYSeriesCollection createDataset(List<CurrencyDataForResultOutput> testData, RateType rateType);
}