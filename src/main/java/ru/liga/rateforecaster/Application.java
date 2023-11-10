package ru.liga.rateforecaster;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.rateforecaster.data.factory.CurrencyPathResolver;
import ru.liga.rateforecaster.forecast.UserRequestForecastGenerator;
import ru.liga.rateforecaster.forecast.algorithm.factory.GenericPredictionAlgorithm;
import ru.liga.rateforecaster.telegrambot.Bot;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotDialogHandler;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotDialogHandlerImpl;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotKeyboardFactory;
import ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotRequestHandler;
import ru.liga.rateforecaster.telegrambot.sender.TelegramMessageSender;
import ru.liga.rateforecaster.telegrambot.sender.TelegramMessageSenderImpl;
import ru.liga.rateforecaster.utils.AppConfig;

import java.util.ResourceBundle;


public class Application {

    public static void main(String[] args) throws TelegramApiException {
        runTelegramBot();
    }

    public static TelegramBotsApi runTelegramBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        TelegramBotDialogHandler telegramBotDialogHandler = initializeBotDialogHandler(bot);
        bot.setDialogHandler(telegramBotDialogHandler);
        botsApi.registerBot(bot);
        return botsApi;
    }

    public static ResourceBundle initializeBundleWithMessages() {
        return ResourceBundle.getBundle("messages/messages");
    }

    public static ResourceBundle initializeBundleWithErrorMessages() {
        return ResourceBundle.getBundle("messages/errors");
    }

    public static TelegramBotDialogHandler initializeBotDialogHandler(Bot bot) {
        ResourceBundle bundleWithMessages = initializeBundleWithMessages();
        ResourceBundle bundleWithErrorMessages = initializeBundleWithErrorMessages();
        return new TelegramBotDialogHandlerImpl(
                initializeTelegramBotRequestHandler(bot, bundleWithMessages),
                initializeTelegramMessageSender(bot, bundleWithErrorMessages),
                initializeBundleWithMessages(),
                initializeBundleWithErrorMessages());
    }

    public static TelegramBotRequestHandler initializeTelegramBotRequestHandler(Bot bot, ResourceBundle bundleWithMessages) {
        return new TelegramBotRequestHandler(
                bot,
                new TelegramBotKeyboardFactory(bundleWithMessages),
                bundleWithMessages
        );
    }

    public static TelegramMessageSender initializeTelegramMessageSender(Bot bot, ResourceBundle bundleWithErrorMessages) {
        return new TelegramMessageSenderImpl(
                bot,
                bundleWithErrorMessages,
                new UserRequestForecastGenerator(bundleWithErrorMessages, new GenericPredictionAlgorithm(),
                        new CurrencyPathResolver(AppConfig.getInstance()))
        );
    }

}