package ru.liga.rateforecaster.telegrambot.dialoghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.telegrambot.Bot;

import java.util.List;
import java.util.ResourceBundle;

import static ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotKeyboardFactory.*;
import static ru.liga.rateforecaster.telegrambot.dialoghandler.TelegramBotKeyboardFactory.createPeriodOrDateKeyboard;

/**
 * Handles sending various types of keyboard messages in the Telegram bot.
 */
public class TelegramBotRequestHandler {
    private final Bot bot;
    private final ResourceBundle resourceBundle;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotRequestHandler.class);

    public TelegramBotRequestHandler(Bot bot, ResourceBundle resourceBundle) {
        this.bot = bot;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Opens the output keyboard for selecting forecast options.
     *
     * @param update The incoming update from Telegram.
     * @param period The forecast period to be displayed on the keyboard.
     */
    void openOutputKeyboard(Update update, RateType period) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("forecast.output.type.message"));
        sendMessage.setReplyMarkup(createOutputKeyboard(period));

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error while sending message with output keyboard.", e);
        }
    }

    /**
     * Opens the algorithm selection keyboard.
     *
     * @param update The incoming update from Telegram.
     */
    void openAlgorithmKeyboard(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("choose.algorithm.message"));
        sendMessage.setReplyMarkup(createAlgorithmKeyboard());

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error while sending message with algorithm keyboard.", e);
        }
    }

    /**
     * Opens the currency selection keyboard.
     *
     * @param update     The incoming update from Telegram.
     * @param currencies The list of available currencies.
     */
    void openCurrencyKeyboard(Update update, List<Currency> currencies) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("choose.currencies"));
        sendMessage.setReplyMarkup(createCurrencyKeyboard(currencies));

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error while sending message with currency keyboard.", e);
        }
    }

    /**
     * Opens the rate type selection keyboard.
     *
     * @param update The incoming update from Telegram.
     */
    void openRateTypeKeyboard(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(resourceBundle.getString("choose.rate.type.message"));
        sendMessage.setReplyMarkup(createPeriodOrDateKeyboard());

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error while sending message with rate type keyboard.", e);
        }
    }
}