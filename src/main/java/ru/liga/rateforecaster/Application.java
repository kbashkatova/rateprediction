package ru.liga.rateforecaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.rateforecaster.forecast.UserRequestForecastGenerator;
import ru.liga.rateforecaster.telegrambot.Bot;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotDialogHandlerImpl;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotKeyboardFactory;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotRequestHandler;
import ru.liga.rateforecaster.telegrambot.sender.TelegramMessageSenderImpl;

import java.util.ResourceBundle;


public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("messages/messages");
            bot.setDialogHandler(new TelegramBotDialogHandlerImpl(
                    new TelegramBotRequestHandler(bot, new TelegramBotKeyboardFactory(resourceBundle), resourceBundle),
                    new TelegramMessageSenderImpl(bot, resourceBundle, new UserRequestForecastGenerator(resourceBundle))
            ));
            botsApi.registerBot(bot);
        } catch (Exception e) {
            logger.error("An error occurred during bot registration", e);
        }
    }
}