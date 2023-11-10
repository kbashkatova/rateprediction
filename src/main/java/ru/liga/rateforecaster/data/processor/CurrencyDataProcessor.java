package ru.liga.rateforecaster.data.processor;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;

/**
 * The CurrencyDataProcessor class is responsible for processing currency data from a CSV file
 * and providing methods to read the data from resources.
 */
public class CurrencyDataProcessor {

    protected final String filePath;

    public CurrencyDataProcessor(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads currency data from a CSV file located in the classpath resources.
     *
     * @return A linked list of CurrencyData objects containing currency exchange rate data.
     * @throws CsvValidationException if there is an issue with CSV data validation.
     * @throws IOException            if an I/O error occurs while reading the resource.
     */
    public LinkedList<CurrencyData> readCurrencyDataFromResources() throws CsvValidationException, IOException {
        final Logger logger = LoggerFactory.getLogger(CurrencyDataProcessor.class);
        final LinkedList<CurrencyData> currencyDataList = new LinkedList<>();
        try (InputStream resourceStream = getClass().getResourceAsStream(filePath)) {
            if (resourceStream == null) {
                logger.error("Failed to open resource stream for file: {}", filePath);
                throw new IOException("Failed to open resource stream for file: " + filePath);
            }
            final InputStreamReader reader = new InputStreamReader(resourceStream);
            final CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                final String dateStr = nextRecord[1];
                final BigDecimal curs = new BigDecimal(nextRecord[2]);

                final LocalDate date = DateUtils.formatDate(dateStr);

                final CurrencyData currencyData = new CurrencyData(date, curs);
                currencyDataList.add(currencyData);
            }
        } catch (IOException e) {
            logger.error("Failed to read data from resource: {}", e.getMessage());
            throw new IOException("Failed to read data from resource", e);
        }
        return currencyDataList;
    }
}