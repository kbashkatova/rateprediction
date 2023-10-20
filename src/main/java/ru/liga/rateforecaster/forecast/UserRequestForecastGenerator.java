package ru.liga.rateforecaster.forecast;

import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.rateforecaster.forecast.generator.CurrencyForecastGenerator;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.parser.ConsoleInputReader;
import ru.liga.rateforecaster.parser.RequestParser;

import java.io.IOException;
import java.text.ParseException;

import static java.util.Objects.isNull;
import static ru.liga.rateforecaster.forecast.generator.CurrencyForecastGenerator.selectForecastGenerator;

/**
 * The UserRequestForecastGenerator class provides methods for generating currency forecasts based on user requests.
 * It parses the user request, selects the appropriate forecast generator, and generates the forecast.
 */
public class UserRequestForecastGenerator {
    private static final String ERROR_REQUEST_MASSAGE = "Проверьте запрос на корректность.";
    private static final Logger logger = LoggerFactory.getLogger(UserRequestForecastGenerator.class);

    /**
     * Processes a user's request to generate a currency forecast.
     * This method reads a user's request, checks for errors, and generates a currency forecast based on the request.
     * @return A String containing the generated currency forecast or an error message in case of failure.
     */
    public static String proceedUserRequest() {
        ParsedRequest parsedRequest = getParsedRequest();
        return generateForecast(parsedRequest);
    }

    private static ParsedRequest getParsedRequest() {
        ParsedRequest parsedRequest = null;
        ConsoleInputReader consoleInputReader = new ConsoleInputReader();
        String request = requestChecker(consoleInputReader.readConsoleInput());
        while (isNull(parsedRequest)) {
            System.out.println(ERROR_REQUEST_MASSAGE);
            consoleInputReader = new ConsoleInputReader();
            request = requestChecker(consoleInputReader.readConsoleInput());
            parsedRequest = new RequestParser().parseRequest(request);
        }
        return parsedRequest;
    }

    private static String requestChecker(String request) {
        if (isNull(request)) {
            return null;
        } else {
            return checkForExit(request);
        }
    }

    private static String checkForExit(String request) {
        if (request.equals("exit")) {
            logger.info("Вызод из программы");
            System.exit(0);
        }
        return request;
    }

    /**
     * Generates a currency forecast based on the user's request.
     *
     * @param parsedRequest The parsed user request.
     * @return A String containing the generated currency forecast or an error message in case of failure.
     */
    public static String generateForecast(ParsedRequest parsedRequest) {
        if (isNull(parsedRequest.getCurrency()) || isNull(parsedRequest.getRateType())) {
           return ERROR_REQUEST_MASSAGE;
        }
        try {
            final CurrencyForecastGenerator forecast = selectForecastGenerator(parsedRequest.getRateType());
            return forecast.generateForecast(parsedRequest.getCurrency());
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
