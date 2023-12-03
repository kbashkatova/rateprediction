package ru.liga.rateforecaster.telegrambot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.rateforecaster.utils.AppConfig;

/**
 * Telegram bot class that handles user interactions.
 */
public final class Bot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private TelegramBotDialogHandler telegramBotDialogHandler;

    public Bot() {
    }

    public void setDialogHandler(TelegramBotDialogHandler dialogHandler) {
        this.telegramBotDialogHandler = dialogHandler;
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
}