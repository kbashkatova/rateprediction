package ru.liga.rateforecaster.telegrambot.dialoghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.liga.rateforecaster.enums.Algorithm;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.utils.AppConfig;

import java.util.*;

public class TelegramBotKeyboardFactory {

    private static final ResourceBundle resourceBundle = loadResourceBundle();
    private static final Logger log = LoggerFactory.getLogger(TelegramBotKeyboardFactory.class);



    /**
     * Creates a keyboard for selecting currencies.
     *
     * @param currencies The list of selected currencies
     * @return A ReplyKeyboardMarkup object with the currency keyboard
     */
    public static ReplyKeyboardMarkup createCurrencyKeyboard(List<Currency> currencies) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            for (Currency currency : Currency.values()) {
                KeyboardRow row = new KeyboardRow();
                KeyboardButton button = new KeyboardButton(currency.name());
                if (!currencies.contains(currency)) {
                    row.add(button);
                    keyboardRows.add(row);
                }
            }

            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton(resourceBundle.getString("choose.rate.type.message"));
            row.add(button);
            keyboardRows.add(row);
        } catch (MissingResourceException e) {
            log.error("Error while creating currency keyboard.", e);
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

    /**
     * Creates a keyboard for selecting the forecast period or entering a date.
     *
     * @return A ReplyKeyboardMarkup object with the period or date keyboard
     */
    public static ReplyKeyboardMarkup createPeriodOrDateKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        List<RateType> rateTypes = Arrays.stream(RateType.values())
                .filter(rateType -> !rateType.equals(RateType.DAY))
                .toList();
        try {
            for (RateType rateType : rateTypes) {
                KeyboardRow row = new KeyboardRow();
                KeyboardButton button = new KeyboardButton(rateType.name());
                row.add(button);
                keyboardRows.add(row);
            }
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton(resourceBundle.getString("manual.date.input.message"));
            row.add(button);
            keyboardRows.add(row);
        } catch (MissingResourceException e) {
            log.error("Error while creating period or date keyboard.", e);
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }


    /**
     * Creates a keyboard for selecting the forecast algorithm.
     *
     * @return A ReplyKeyboardMarkup object with the algorithm keyboard
     */
    public static ReplyKeyboardMarkup createAlgorithmKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        try {
            for (Algorithm algorithm : Algorithm.values()) {
                KeyboardRow row = new KeyboardRow();
                KeyboardButton button = new KeyboardButton(algorithm.name());
                row.add(button);
                keyboardRows.add(row);
            }
        } catch (MissingResourceException e) {
            log.error("Error while creating algorithm keyboard.", e);
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

    /**
     * Creates a keyboard for selecting the output type based on the forecast period.
     *
     * @param period The forecast period type
     * @return A ReplyKeyboardMarkup object with the output type keyboard
     */
    public static ReplyKeyboardMarkup createOutputKeyboard(RateType period) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        List<OutputType> outputTypes = getOutputTypesForPeriod(period);
        try {
            for (OutputType outputType : outputTypes) {
                KeyboardRow row = new KeyboardRow();
                KeyboardButton button = new KeyboardButton(outputType.name());
                row.add(button);
                keyboardRows.add(row);
            }
        } catch (MissingResourceException e) {
            log.error("Error while creating output keyboard.", e);
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setOneTimeKeyboard(true);
        return keyboard;
    }

    private static List<OutputType> getOutputTypesForPeriod(RateType period) {
        if (period.equals(RateType.DAY)) {
            return Arrays.stream(OutputType.values())
                    .filter(outputType -> !outputType.equals(OutputType.GRAPH))
                    .toList();
        } else {
            return Arrays.stream(OutputType.values()).toList();
        }
    }

    private static ResourceBundle loadResourceBundle() {
        AppConfig appConfig = AppConfig.getInstance();
        String locale = appConfig.getLocale();
        return ResourceBundle.getBundle("messages/messages", new Locale(locale));
    }
}
