package output;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.formatter.outputgenerator.StringOutputGenerator;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.model.ParsedRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringOutputGeneratorTest {

    private StringOutputGenerator stringOutputGenerator;

    @BeforeEach
    public void setUp() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages/messages");
        stringOutputGenerator = new StringOutputGenerator(resourceBundle);
    }


    @Test
    void testGenerateDayOutput() {
        Currency currency = Currency.USD;
        LocalDate date = LocalDate.of(2023, 10, 29);
        List<CurrencyData> forecastData = new ArrayList<>();
        forecastData.add(new CurrencyData(date, new BigDecimal("1.2345")));

        List<CurrencyDataForResultOutput> testData = new ArrayList<>();
        testData.add(new CurrencyDataForResultOutput(currency, forecastData));

        ParsedRequest parsedRequest = new ParsedRequest(List.of(currency), null, RateType.DAY, null, OutputType.LIST);
        FormattedResult result = stringOutputGenerator.format(testData, parsedRequest);

        String expected = "rate USD tomorrow вс 29.10.2023 - 1,23;";
        assertEquals(expected, result.getTextResult());
    }

    @Test
    void format_WithWeekRateType_ReturnsValidWeekOutput() {
        List<CurrencyDataForResultOutput> testData = generateTestData();
        ParsedRequest parsedRequest = new ParsedRequest(null, null, RateType.WEEK, null, OutputType.LIST);
        FormattedResult result = stringOutputGenerator.format(testData, parsedRequest);

        String expectedOutput = "rate USD week\n" +
                "пн 23.10.2023 - 75,40\n" +
                "вт 24.10.2023 - 75,50\n" +
                "ср 25.10.2023 - 75,30\n";

        assertEquals(expectedOutput, result.getTextResult());
    }

    @Test
    void format_WithMonthRateType_ReturnsValidMonthOutput() {
        List<CurrencyDataForResultOutput> testData = generateTestData();
        ParsedRequest parsedRequest = new ParsedRequest(null, null, RateType.MONTH, null, OutputType.LIST);
        FormattedResult result = stringOutputGenerator.format(testData, parsedRequest);

        String expectedOutput = "rate USD month\n" +
                "пн 23.10.2023 - 75,40\n" +
                "вт 24.10.2023 - 75,50\n" +
                "ср 25.10.2023 - 75,30\n";

        assertEquals(expectedOutput, result.getTextResult());
    }

    private List<CurrencyDataForResultOutput> generateTestData() {
        List<CurrencyDataForResultOutput> testData = new ArrayList<>();

        List<CurrencyData> forecastData = new ArrayList<>();
        forecastData.add(new CurrencyData(LocalDate.of(2023, 10, 25), new BigDecimal("75.30")));
        forecastData.add(new CurrencyData(LocalDate.of(2023, 10, 24), new BigDecimal("75.50")));
        forecastData.add(new CurrencyData(LocalDate.of(2023, 10, 23), new BigDecimal("75.40")));

        testData.add(new CurrencyDataForResultOutput(Currency.USD, forecastData));

        return testData;
    }

}
