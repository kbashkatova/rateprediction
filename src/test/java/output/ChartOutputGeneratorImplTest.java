package output;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.formatter.outputgenerator.ChartOutputGenerator;
import ru.liga.rateforecaster.formatter.outputgenerator.ChartOutputGeneratorImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChartOutputGeneratorImplTest {

    private ChartOutputGenerator chartGenerator;

    @BeforeEach
    void setup() {
        chartGenerator = new ChartOutputGeneratorImpl(ResourceBundle.getBundle("messages/messages", new Locale("ru")));
    }

    @Test
    void createChart_WithTestData_ReturnsValidChartPanel() {
        List<CurrencyDataForResultOutput> testData = generateTestData();
        RateType rateType = RateType.WEEK;
        ChartPanel chartPanel = chartGenerator.createChart(testData, rateType);

        assertNotNull(chartPanel);
    }

    @Test
    void createDataset_WithTestDataAndWeekRateType_ReturnsValidDataset() {
        List<CurrencyDataForResultOutput> testData = generateTestData();
        RateType rateType = RateType.WEEK;
        XYSeriesCollection dataset = chartGenerator.createDataset(testData, rateType);
        assertNotNull(dataset);
    }


    private List<CurrencyDataForResultOutput> generateTestData() {
        List<CurrencyDataForResultOutput> testData = new ArrayList<>();
        return testData;
    }
}