package ru.liga.rateforecaster.telegrambot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotDialogHandlerImpl;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotRequestHandler;
import ru.liga.rateforecaster.telegrambot.model.BotState;
import ru.liga.rateforecaster.telegrambot.sender.TelegramMessageSender;
import ru.liga.rateforecaster.telegrambot.sender.TelegramMessageSenderImpl;
import ru.liga.rateforecaster.utils.AppConfig;

import java.util.ResourceBundle;

/**
 * Telegram bot class that handles user interactions.
 */
public final class Bot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private final TelegramBotDialogHandler telegramBotDialogHandler;
    private final TelegramMessageSender telegramMessageSender;

    /**
     * Constructs the Telegram bot.
     *
     * @param resourceBundle The ResourceBundle for localization.
     */
    public Bot(ResourceBundle resourceBundle) {
        TelegramBotRequestHandler telegramBotRequestHandler = new TelegramBotRequestHandler(this, resourceBundle);
        this.telegramBotDialogHandler = new TelegramBotDialogHandlerImpl(this, telegramBotRequestHandler);
        this.telegramMessageSender = new TelegramMessageSenderImpl(this, resourceBundle);
    }
    @Override
    public String getBotUsername() {
        return AppConfig.getInstance().getBotUserName();
    }

    @Override
    public String getBotToken() {
        return AppConfig.getInstance().getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Received an update: {}", update);
        telegramBotDialogHandler.handleUpdate(update);
    }

    /**
     * Handles a user request.
     *
     * @param botState The current state of the bot.
     * @return A formatted result.
     */
    public FormattedResult handleUserRequest(BotState botState) {
        log.info("Handling user request: {}", botState);
        return telegramMessageSender.handleUserRequest(botState);
    }

    /**
     * Sends a result message to the user.
     *
     * @param result The result to send.
     * @param update The update to reply to.
     */
    public void sendMessageResult(FormattedResult result, Update update) {
        log.info("Sending a result message to the user.");
        telegramMessageSender.sendMessageResult(result, update);
    }

    /**
     * Sends a message to the user.
     *
     * @param message The message to send.
     * @param update  The update to reply to.
     */
    public void sendMessage(String message, Update update) {
        log.info("Sending a message to the user.");
        telegramMessageSender.sendMessage(message, update);
    }

    /**
     * Sends an error message to the user.
     *
     * @param errorMessage The error message to send.
     * @param chatId       The chat ID.
     */
    public void sendError(String errorMessage, Long chatId) {
        log.error("Sending an error message to the user: {}", errorMessage);
        telegramMessageSender.sendError(errorMessage, chatId);
    }

    /**
     * Exits the program with a provided exit message.
     *
     * @param exitMessage The exit message.
     * @param update      The update for the exit message.
     */
    public void exitProgram(String exitMessage, Update update) {
        log.info("Exiting the program: {}", exitMessage);
        sendMessage(exitMessage, update);
        System.exit(0);
    }
}