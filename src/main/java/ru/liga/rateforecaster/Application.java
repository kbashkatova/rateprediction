package ru.liga.rateforecaster;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.rateforecaster.telegrambot.Bot;
import ru.liga.rateforecaster.utils.AppConfig;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * This is the main class responsible for running the currency forecast application.
 */
public class Application {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot(ResourceBundle.getBundle("messages/messages",
                    new Locale(AppConfig.getInstance().getLocale()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}