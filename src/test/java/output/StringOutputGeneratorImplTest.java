package output;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.formatter.model.CurrencyDataForResultOutput;
import ru.liga.rateforecaster.formatter.outputgenerator.StringOutputGenerator;
import ru.liga.rateforecaster.formatter.outputgenerator.StringOutputGeneratorImpl;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringOutputGeneratorImplTest {

    private StringOutputGenerator stringGenerator;
    private ResourceBundle resourceBundle;

    @BeforeEach
    void setup() {
        resourceBundle = ResourceBundle.getBundle("messages/messages", new Locale("ru"));
        stringGenerator = new StringOutputGeneratorImpl(resourceBundle);
    }

    @Test
    void testGenerateDayOutput() {
        StringOutputGeneratorImpl generator = new StringOutputGeneratorImpl(resourceBundle);
        Currency currency = Currency.USD;
        LocalDate date = LocalDate.of(2023, 10, 29);
        List<CurrencyData> forecastData = new ArrayList<>();
        forecastData.add(new CurrencyData(date, new BigDecimal("1.2345")));

        String expected = "rate USD tomorrow вс 29.10.2023 - 1,23;";
        String result = generator.generateDayOutput(currency, forecastData);

        assertEquals(expected, result);
    }

    @Test
    void testGenerateWeekOutput() {
        StringOutputGeneratorImpl generator = new StringOutputGeneratorImpl(resourceBundle);
        Currency currency = Currency.USD;
        List<CurrencyData> forecastData = new ArrayList<>();
        LocalDate date1 = LocalDate.of(2023, 10, 29);
        LocalDate date2 = LocalDate.of(2023, 10, 30);
        forecastData.add(new CurrencyData(date1, new BigDecimal("1.2345")));
        forecastData.add(new CurrencyData(date2, new BigDecimal("1.3456")));

        String expected = "rate USD week\nпн 30.10.2023 - 1,35\nвс 29.10.2023 - 1,23\n";
        String result = generator.generateWeekOutput(currency, forecastData);

        assertEquals(expected, result);
    }

    @Test
    void testGenerateMonthOutput() {
        StringOutputGeneratorImpl generator = new StringOutputGeneratorImpl(resourceBundle);
        Currency currency = Currency.USD;
        List<CurrencyData> forecastData = new ArrayList<>();
        LocalDate date1 = LocalDate.of(2023, 10, 29);
        LocalDate date2 = LocalDate.of(2023, 10, 30);
        forecastData.add(new CurrencyData(date1, new BigDecimal("1.2345")));
        forecastData.add(new CurrencyData(date2, new BigDecimal("1.3456")));

        String expected = "rate USD month\nпн 30.10.2023 - 1,35\nвс 29.10.2023 - 1,23\n";
        String result = generator.generateMonthOutput(currency, forecastData);

        assertEquals(expected, result);
    }
}