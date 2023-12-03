package ru.liga.rateforecaster.telegrambot.sender;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.telegrambot.model.BotState;

/**
 * The interface for sending messages in a Telegram bot.
 */
public interface TelegramMessageSender {

    /**
     * Handles a user request and generates a formatted result.
     *
     * @param botState The current state of the bot.
     * @return A formatted result based on the user's request.
     */
    FormattedResult handleUserRequest(BotState botState);

    /**
     * Sends a formatted result message to the user.
     *
     * @param result The formatted result to send.
     * @param update The update object to reply to.
     */
    void sendMessageResult(FormattedResult result, Update update);

    /**
     * Sends a text message to the user.
     *
     * @param message The text message to send.
     * @param update  The update object to reply to.
     */
    void sendMessage(String message, Update update);

    /**
     * Sends an error message to the user with a specified chat ID.
     *
     * @param errorMessage The error message to send.
     * @param chatId       The chat ID of the user to send the error message to.
     */
    void sendError(String errorMessage, Long chatId);

    void exitProgram(String exitMessage, Update update);
}