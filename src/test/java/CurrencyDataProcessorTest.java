import com.opencsv.exceptions.CsvValidationException;
import ru.liga.rateforecaster.data.processor.CurrencyDataProcessor;
import ru.liga.rateforecaster.model.CurrencyData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CurrencyDataProcessorTest {
    @Test
    public void testReadUSDDataFromResources() throws CsvValidationException, IOException {
        String filePath = "/USD.csv";
        String currencyName = "Доллар США";
        CurrencyDataProcessor dataProcessor = new CurrencyDataProcessor(filePath, currencyName);

        LinkedList<CurrencyData> currencyDataList = dataProcessor.readCurrencyDataFromResources();

        assertNotNull(currencyDataList);

        assertCurrencyData(currencyDataList.get(0), "10/13/2023", 96.9948);
        assertCurrencyData(currencyDataList.get(1), "10/12/2023", 99.9808);
        assertCurrencyData(currencyDataList.get(2), "10/11/2023", 99.9349);
    }

    private void assertCurrencyData(CurrencyData currencyData, String expectedDate, double expectedRate) {
        assertEquals(LocalDate.parse(expectedDate, DateTimeFormatter.ofPattern("MM/dd/yyyy")), currencyData.getDate());
        assertEquals(expectedRate, currencyData.getRate(), 0.001);
    }
}
