package ru.liga.rateforecaster.formatter.outputgenerator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.formatter.model.GraphColor;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Implementation of the {@link OutputGenerator} interface for generating charts.
 */
public class ChartOutputGenerator implements OutputGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ChartOutputGenerator.class);
    public static final String Y_AXIS_LABEL = "Rate";
    private static final int WEEK_DAYS = 7;
    private static final int MONTH_DAYS = 30;



    /**
     * Format the data into a chart based on the provided rate type and currency data.
     *
     * @param currencyDataForResultOutput List of data to be formatted.
     * @param parsedRequest               The parsed request containing the rate type.
     * @return A {@link FormattedResult} object containing the chart.
     */
    @Override
    public FormattedResult format(List<CurrencyDataForResultOutput> currencyDataForResultOutput, ParsedRequest parsedRequest) {
        logger.info("Creating a chart for {} currency data.", currencyDataForResultOutput.size());

        XYSeriesCollection dataset = createDataset(currencyDataForResultOutput, parsedRequest.rateType());
        JFreeChart chart = createChart(dataset);
        customizeChartRenderer(chart, currencyDataForResultOutput.size());

        return new FormattedResult(new ChartPanel(chart));
    }

    public XYSeriesCollection createDataset(List<CurrencyDataForResultOutput> currencyDataForResultOutput, RateType rateType) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        int maxItems = (rateType == RateType.WEEK) ? WEEK_DAYS : MONTH_DAYS;
        for (CurrencyDataForResultOutput resultOutput : currencyDataForResultOutput) {
            XYSeries series = createSeries(resultOutput, maxItems);
            dataset.addSeries(series);
        }

        return dataset;
    }

    private XYSeries createSeries(CurrencyDataForResultOutput resultOutput, int maxItems) {
        XYSeries series = new XYSeries(resultOutput.getCurrency().toString());
        List<CurrencyData> forecastData = resultOutput.getForecastData();

        for (int j = 0; j < forecastData.size() && j < maxItems; j++) {
            CurrencyData data = forecastData.get(j);
            series.add(j + 1, data.rate().doubleValue());
        }

        return series;
    }

    private JFreeChart createChart(XYSeriesCollection dataset) {
        String GRAPH_NAME = "Currency Rate Forecast";
        String x_AXIS_LABEL = "Date";
        JFreeChart chart = ChartFactory.createXYLineChart(
                GRAPH_NAME,
                x_AXIS_LABEL,
                Y_AXIS_LABEL,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        return chart;
    }

    private void customizeChartRenderer(JFreeChart chart, int seriesCount) {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYItemRenderer renderer = plot.getRenderer();
        XYLineAndShapeRenderer xyRenderer = (XYLineAndShapeRenderer) renderer;

        for (int i = 0; i < seriesCount; i++) {
            Paint seriesColor = GraphColor.getRandomColor();
            xyRenderer.setSeriesPaint(i, seriesColor);
        }
    }
}
