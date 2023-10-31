package ru.liga.rateforecaster.telegrambot.dialoghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.rateforecaster.enums.Algorithm;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.telegrambot.Bot;
import ru.liga.rateforecaster.telegrambot.model.BotState;
import ru.liga.rateforecaster.telegrambot.model.State;
import ru.liga.rateforecaster.utils.AppConfig;
import ru.liga.rateforecaster.utils.DateUtils;

import java.util.*;

import static java.util.Objects.isNull;
import static ru.liga.rateforecaster.telegrambot.model.State.*;

/**
 * The handler for user dialog in the Telegram bot.
 */
public class TelegramBotDialogHandlerImpl implements TelegramBotDialogHandler {

    private final Bot bot;
    private final TelegramBotRequestHandler telegramBotRequestHandler;
    private BotState botState;
    private static final Logger log = LoggerFactory.getLogger(TelegramBotDialogHandlerImpl.class);

    private static final ResourceBundle messageResourceBundle = loadResourceBundle();
    private static final ResourceBundle errorResourceBundle = loadErrorResourceBundle();


    private static final String START_COMMAND = "/start";
    private static final String HELP_COMMAND = "/help";

    private static final String EXIT_COMMAND = "/exit";
    private static final String TOMORROW_COMMAND = "tomorrow";

    public TelegramBotDialogHandlerImpl(Bot bot, TelegramBotRequestHandler telegramBotRequestHandler) {
        this.bot = bot;
        this.telegramBotRequestHandler = telegramBotRequestHandler;
    }

    /**
     * Handles an incoming update.
     *
     * @param update The update from Telegram.
     */
    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();

            if (START_COMMAND.equals(messageText)) {
                log.info("Received '/start' command from user.");
                startNewConversation(update);
                initializeBot(update);
            } else if (HELP_COMMAND.equals(messageText)) {
                log.info("Received '/help' command from user.");
                showHelp(update);
            } else if (EXIT_COMMAND.equals(messageText)) {
                log.info("Received '/exit' command from user.");
                bot.exitProgram(messageResourceBundle.getString("exit"), update);
            } else {
                log.info("Received a user message: '{}'", messageText);
                handleUserMessage(messageText, update);
            }
        }
    }

    private void handleUserMessage(String messageText, Update update) {
        switch (botState.getState()) {
            case WAITING_FOR_CURRENCY:
                handleCurrencySelection(messageText, update, botState.getCurrencies());
                break;
            case WAITING_FOR_PERIOD:
                handlePeriodSelection(messageText, update);
                break;
            case WAITING_FOR_DATE_INPUT:
                handleDateInput(messageText, update);
                break;
            case WAITING_FOR_ALGORITHM:
                handleAlgorithmSelection(messageText, update);
                break;
            case WAITING_FOR_OUTPUT:
                handleOutputSelection(messageText);
                handleRequestAndRestart(update);
                initializeBot(update);
                break;
        }
    }

    private void initializeBot(Update update) {
        botState = new BotState();
        botState.setState(WAITING_FOR_CURRENCY);
        telegramBotRequestHandler.openCurrencyKeyboard(update, Collections.emptyList());

    }

    private void startNewConversation(Update update) {
        String instructions = messageResourceBundle.getString("welcome.message");
        bot.sendMessage(instructions, update);
    }

    private void handleCurrencySelection(String messageText, Update update, List<Currency> currencies) {
        if (!Objects.equals(messageText, messageResourceBundle.getString("choose.rate.type.message"))) {
            botState.getCurrencies().add(Currency.valueOf(messageText));
            botState.setState(WAITING_FOR_CURRENCY);
            telegramBotRequestHandler.openCurrencyKeyboard(update, currencies);
        } else {
            botState.setState(WAITING_FOR_PERIOD);
            telegramBotRequestHandler.openRateTypeKeyboard(update);
        }
    }

    private void handlePeriodSelection(String messageText, Update update) {
        if (messageText.equals(messageResourceBundle.getString("manual.date.input.message"))) {
            botState.setState(State.WAITING_FOR_DATE_INPUT);
        } else {
            botState.setPeriod(RateType.valueOf(messageText));
            botState.setState(State.WAITING_FOR_ALGORITHM);
            telegramBotRequestHandler.openAlgorithmKeyboard(update);
        }
    }

    private void handleDateInput(String messageText, Update update) {
        if (messageText.equals(TOMORROW_COMMAND)) {
            botState.setDate(Optional.of(DateUtils.getTomorrowDate()));
            botState.setState(State.WAITING_FOR_ALGORITHM);
            telegramBotRequestHandler.openAlgorithmKeyboard(update);
        } else {
            try {
                botState.setDate(Optional.of(DateUtils.parseDate(messageText)));
                botState.setState(State.WAITING_FOR_ALGORITHM);
                telegramBotRequestHandler.openAlgorithmKeyboard(update);
            } catch (RuntimeException e) {
                log.error("Date parse error: {}", e.getMessage());
                bot.sendMessage(errorResourceBundle.getString("date.parse.error.message"), update);
                botState.setState(WAITING_FOR_DATE_INPUT);
            }

        }
    }

    private void handleAlgorithmSelection(String messageText, Update update) {
        botState.setAlgorithm(Algorithm.valueOf(messageText));
        botState.setState(State.WAITING_FOR_OUTPUT);
        if (isNull(botState.getPeriod())) {
            botState.setPeriod(RateType.DAY);
        }
        telegramBotRequestHandler.openOutputKeyboard(update, botState.getPeriod());
    }

    private void handleOutputSelection(String messageText) {
        botState.setOutputType(OutputType.valueOf(messageText));
    }

    private void handleRequestAndRestart(Update update) {
        FormattedResult result = bot.handleUserRequest(botState);
        bot.sendMessageResult(result, update);
        bot.sendMessage(messageResourceBundle.getString("success.message"), update);
    }

    private static ResourceBundle loadResourceBundle() {
        AppConfig appConfig = AppConfig.getInstance();
        String locale = appConfig.getLocale();
        return ResourceBundle.getBundle("messages/messages", new Locale(locale));
    }

    private static ResourceBundle loadErrorResourceBundle() {
        AppConfig appConfig = AppConfig.getInstance();
        String locale = appConfig.getLocale();
        return ResourceBundle.getBundle("messages/errors", new Locale(locale));
    }

    private void showHelp(Update update) {
        StringBuilder helpMessage = new StringBuilder(messageResourceBundle.getString("bot.helpMessage"));

        if (!botState.getCurrencies().isEmpty()) {
            helpMessage.append(messageResourceBundle.getString("bot.currencies")).append(botState.getCurrencies()).append("\n");
        }

        if (botState.getPeriod() != null) {
            helpMessage.append(messageResourceBundle.getString("bot.period")).append(botState.getPeriod()).append("\n");
        }

        if (botState.getDate().isPresent()) {
            helpMessage.append(messageResourceBundle.getString("bot.date")).append(botState.getDate().get()).append("\n");
        }

        if (botState.getAlgorithm() != null) {
            helpMessage.append(messageResourceBundle.getString("bot.algorithm")).append(botState.getAlgorithm()).append("\n");
        }

        if (botState.getOutputType() != null) {
            helpMessage.append(messageResourceBundle.getString("bot.outputType")).append(botState.getOutputType()).append("\n");
        }

        bot.sendMessage(helpMessage.toString(), update);
    }
}
