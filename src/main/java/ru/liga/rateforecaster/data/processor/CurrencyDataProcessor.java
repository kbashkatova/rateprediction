package ru.liga.rateforecaster.data.processor;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import ru.liga.rateforecaster.model.CurrencyData;
import ru.liga.rateforecaster.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;

/**
 * The CurrencyDataProcessor class is responsible for processing currency data from a CSV file
 * and providing methods to read the data from resources.
 */
public class CurrencyDataProcessor {

    protected String filePath;
    protected String currencyName;

    public CurrencyDataProcessor(String filePath, String currencyName) {
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
        Logger logger = LoggerFactory.getLogger(CurrencyDataProcessor.class);
        LinkedList<CurrencyData> currencyDataList = new LinkedList<>();
        try (InputStream resourceStream = getClass().getResourceAsStream(filePath)) {
            if (resourceStream == null) {
                logger.error("Failed to open resource stream for file: {}", filePath);
                throw new IOException("Failed to open resource stream for file: " + filePath);
            }
            InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(resourceStream));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                String dateStr = nextRecord[1];
                double curs = Double.parseDouble(nextRecord[2]);

                LocalDate date = DateUtils.formatDate(dateStr);

                CurrencyData currencyData = new CurrencyData(date, curs);
                currencyDataList.add(currencyData);
            }
        } catch (IOException e) {
            logger.error("Failed to read data from resource: {}", e.getMessage(), e);
            throw new IOException("Failed to read data from resource", e);
        }
        return currencyDataList;
    }
}