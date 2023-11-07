package output;

import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.formatter.outputgenerator.ChartOutputGenerator;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChartOutputGeneratorTest {

    private ChartOutputGenerator chartOutputGenerator;

    @BeforeEach
    public void setUp() {
        chartOutputGenerator = new ChartOutputGenerator();
    }
    @Test
    void createChart_WithTestData_ReturnsValidChartPanel() {
        List<CurrencyDataForResultOutput> testData = generateTestData();
        FormattedResult formattedResult = chartOutputGenerator.format(testData,
                new ParsedRequest(null, null, RateType.WEEK, null, OutputType.GRAPH));

        assertNotNull(formattedResult.getChartImage());
    }


    @Test
    void createDataset_WithTestDataAndWeekRateType_ReturnsValidDataset() {
        List<CurrencyDataForResultOutput> testData = generateTestData();
        RateType rateType = RateType.WEEK;
        XYSeriesCollection dataset = chartOutputGenerator.createDataset(testData, rateType);
        assertNotNull(dataset);
    }


    private List<CurrencyDataForResultOutput> generateTestData() {
        List<CurrencyDataForResultOutput> testData = new ArrayList<>();
        return testData;
    }
}
