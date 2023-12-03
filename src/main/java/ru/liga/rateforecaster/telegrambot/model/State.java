package ru.liga.rateforecaster.telegrambot.model;


/**
 * Enum representing different states of the Telegram bot conversation.
 */
public enum State {
    WAITING_FOR_CURRENCY,
    WAITING_FOR_PERIOD,
    WAITING_FOR_ALGORITHM,
    WAITING_FOR_DATE_INPUT,
    WAITING_FOR_OUTPUT
}