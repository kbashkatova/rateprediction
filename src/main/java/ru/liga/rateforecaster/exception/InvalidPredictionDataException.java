package ru.liga.rateforecaster.exception;

/**
 * An exception that represents an invalid prediction data error.
 */
public class InvalidPredictionDataException extends RuntimeException {
    public InvalidPredictionDataException(String message) {

        super(message);
    }
}