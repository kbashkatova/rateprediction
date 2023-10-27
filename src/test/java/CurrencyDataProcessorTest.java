import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.liga.rateforecaster.data.processor.CurrencyDataProcessor;
import ru.liga.rateforecaster.model.CurrencyData;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CurrencyDataProcessorTest {

    private CurrencyDataProcessor currencyDataProcessor;

    @BeforeEach
    public void setUp() {
        currencyDataProcessor = Mockito.spy(new CurrencyDataProcessor("/TRY.csv"));
    }

    @Test
    public void testReadCurrencyDataFromResources() throws CsvValidationException, IOException {
        LinkedList<CurrencyData> expectedData = createSampleCurrencyData();

        when(currencyDataProcessor.readCurrencyDataFromResources()).thenReturn(expectedData);

        LinkedList<CurrencyData> actualData = currencyDataProcessor.readCurrencyDataFromResources();

        assertEquals(expectedData, actualData);
    }

    private LinkedList<CurrencyData> createSampleCurrencyData() {
        LinkedList<CurrencyData> data = new LinkedList<>();
        data.add(new CurrencyData(LocalDate.of(2022, 1, 1), new BigDecimal("1.0")));
        data.add(new CurrencyData(LocalDate.of(2022, 1, 2), new BigDecimal("1.1")));
        data.add(new CurrencyData(LocalDate.of(2022, 1, 3), new BigDecimal("1.2")));
        return data;
    }
}