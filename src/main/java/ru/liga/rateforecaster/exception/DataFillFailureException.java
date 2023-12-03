package ru.liga.rateforecaster.exception;

/**
 * An exception that represents a failure to fill data.
 */
public class DataFillFailureException extends RuntimeException {
    public DataFillFailureException(String message) {
        super(message);
    }
}
