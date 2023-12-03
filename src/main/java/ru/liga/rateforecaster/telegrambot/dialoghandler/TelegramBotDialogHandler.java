package ru.liga.rateforecaster.telegrambot.dialoghandler;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * This interface defines the contract for handling updates in the Telegram bot.
 */
public interface TelegramBotDialogHandler {

    /**
     * Handles an incoming update.
     *
     * @param update The update from Telegram.
     */
    void handleUpdate(Update update);
}