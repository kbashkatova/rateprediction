package ru.liga.rateforecaster.parser;

import ru.liga.rateforecaster.model.Currency;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.model.RateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A parser for user requests related to currency forecasts.
 */
public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    private static final int ELEMENTS_IN_REQUEST = 3;
    private static final int FIRST_ELEMENT_IN_REQUEST = 0;
    private static final int SECOND_ELEMENT_IN_REQUEST = 1;
    private static final int THIRD_ELEMENT_IN_REQUEST = 2;


    /**
     * Parses a user's request and extracts the requested currency and rate type.
     *
     * @param request The user's request as a string.
     * @return A ParsedRequest object containing the extracted currency and rate type, or null if the request is not correctly formatted.
     */
    public ParsedRequest parseRequest(String request) {
        List<String> requestElements = parseWords(request);

        if (isValidRequest(requestElements)) {
            Currency currency = parseCurrency(requestElements);
            RateType rateType = parseRateType(requestElements);
            return new ParsedRequest(rateType, currency);
        } else {
            logger.error("Failed to parse request: Invalid request format");
            return null;
        }
    }

    private static boolean isValidRequest(List<String> requestElements) {
        return requestElements.size() == ELEMENTS_IN_REQUEST &&
                requestElements.get(FIRST_ELEMENT_IN_REQUEST).equals("rate") &&
                isValidCurrency(requestElements.get(SECOND_ELEMENT_IN_REQUEST)) &&
                isValidRequestType(requestElements.get(THIRD_ELEMENT_IN_REQUEST));
    }

    private static List<String> parseWords(String request) {
        return new ArrayList<>(List.of(request.split(" ")));
    }

    private static RateType parseRateType(List<String> requestElements) {
        return RateType.valueOf(requestElements.get(THIRD_ELEMENT_IN_REQUEST).toUpperCase());
    }

    private static Currency parseCurrency(List<String> requestElements) {
        return Currency.valueOf(requestElements.get(SECOND_ELEMENT_IN_REQUEST));
    }

    private static boolean isValidCurrency(String currencyFromRequest) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equals(currencyFromRequest)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidRequestType(String requestType) {
        for (RateType rateType : RateType.values()) {
            if (rateType.getDescription().equals(requestType)) {
                return true;
            }
        }
        return false;
    }
}
