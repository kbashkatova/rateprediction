package ru.liga.rateforecaster.forecast;

import com.opencsv.exceptions.CsvValidationException;
import ru.liga.rateforecaster.forecast.generator.CurrencyForecastGenerator;
import ru.liga.rateforecaster.model.Currency;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.parser.ConsoleInputReader;
import ru.liga.rateforecaster.parser.RequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;

import static java.util.Objects.isNull;
import static ru.liga.rateforecaster.forecast.generator.CurrencyForecastGenerator.selectForecastGenerator;

/**
 * The UserRequestForecastGenerator class provides a static method for generating currency forecasts based on user requests.
 * It parses the user request, selects the appropriate forecast generator, and generates the forecast.
 */
public class UserRequestForecastGenerator {
    private static final Logger logger = LoggerFactory.getLogger(UserRequestForecastGenerator.class);

    /**
     * Generates a currency forecast based on the user's request.
     *
     * @return A String containing the generated currency forecast or an error message in case of failure.
     */
    public static String generateForecast() {
        ParsedRequest parsedRequest = null;
        ConsoleInputReader consoleInputReader = new ConsoleInputReader();
        parsedRequest = new RequestParser().parseRequest(consoleInputReader.readConsoleInput());
        while (isNull(parsedRequest)) {
            System.out.println("Проверьте запрос на корректность.");
            consoleInputReader = new ConsoleInputReader();
            parsedRequest = new RequestParser().parseRequest(consoleInputReader.readConsoleInput());
        }
        try {
            Currency currency = parsedRequest.getCurrency();
            CurrencyForecastGenerator forecast = selectForecastGenerator(parsedRequest.getRateType());
            return forecast.generateForecast(currency);
        } catch (CsvValidationException e) {
            logger.error("CSV validation error: {}", e.getMessage(), e);
            return "Failed to generate forecast: CSV validation error";
        } catch (ParseException e) {
            logger.error("Parse error: {}", e.getMessage(), e);
            return "Failed to generate forecast: Parse error";
        } catch (IOException e) {
            logger.error("IO error: {}", e.getMessage(), e);
            return "Failed to generate forecast: IO error";
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: {}", e.getMessage(), e);
            return "Failed to generate forecast: Invalid request";
        } catch (Exception e) {
            logger.error("Failed to generate forecast: {}", e.getMessage(), e);
            return "Failed to generate forecast";
        }
    }
}
