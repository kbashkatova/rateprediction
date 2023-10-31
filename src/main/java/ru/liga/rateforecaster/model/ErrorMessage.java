package ru.liga.rateforecaster.model;

import lombok.Getter;

/**
 * A class representing an error message in the application.
 */
@Getter
public class ErrorMessage {

    private final String errorMessageText;


    public ErrorMessage(String errorMessageText) {
        this.errorMessageText = errorMessageText;
    }
}