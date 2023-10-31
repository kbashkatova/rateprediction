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

    import java.awt.*;
    import java.util.List;
    import java.util.ResourceBundle;

    /**
     * Implementation of the {@link ChartOutputGenerator} interface for generating charts.
     */
    public class ChartOutputGeneratorImpl extends BasicResultFormatter implements ChartOutputGenerator {

        private static final Logger logger = LoggerFactory.getLogger(ChartOutputGeneratorImpl.class);


        private final String GRAPH_NAME = "Currency Rate Forecast";
        private final String X_AXIS_LABEL =  "Date";
        public static final String Y_AXIS_LABEL = "Rate";
        private static final int WEEK_DAYS = 7;
        private static final int MONTH_DAYS = 30;
        public ChartOutputGeneratorImpl(ResourceBundle resourceBundle) {
            super(resourceBundle);
        }

        /**
         * Creates a chart based on the provided data.
         *
         * @param currencyDataForResultOutput List of data for generating the chart
         * @param rateType Type of time interval (week or month)
         * @return A {@link ChartPanel} object containing the chart
         */
        @Override
        public ChartPanel createChart(List<CurrencyDataForResultOutput> currencyDataForResultOutput, RateType rateType) {
            logger.info("Creating a chart for {} currency data.", currencyDataForResultOutput.size());

            XYSeriesCollection dataset = createDataset(currencyDataForResultOutput, rateType);
            JFreeChart chart = createChart(dataset);
            customizeChartRenderer(chart, currencyDataForResultOutput.size());

            return new ChartPanel(chart);
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
                series.add(j + 1, data.getRate().doubleValue());
            }

            return series;
        }

        private JFreeChart createChart(XYSeriesCollection dataset) {
            JFreeChart chart = ChartFactory.createXYLineChart(
                    GRAPH_NAME,
                    X_AXIS_LABEL,
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
