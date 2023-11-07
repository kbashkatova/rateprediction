package ru.liga.rateforecaster.telegrambot.dialoghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.ForecastingAlgorithm;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.telegrambot.model.BotState;
import ru.liga.rateforecaster.telegrambot.model.State;
import ru.liga.rateforecaster.telegrambot.sender.TelegramMessageSender;
import ru.liga.rateforecaster.telegrambot.sender.TelegramMessageSenderImpl;
import ru.liga.rateforecaster.utils.AppConfig;
import ru.liga.rateforecaster.utils.DateUtils;

import java.util.*;

import static java.util.Objects.isNull;
import static ru.liga.rateforecaster.telegrambot.model.State.*;

/**
 * The handler for user dialog in the Telegram bot.
 */
public class TelegramBotDialogHandlerImpl implements TelegramBotDialogHandler {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotDialogHandlerImpl.class);
    private final TelegramBotRequestHandler telegramBotRequestHandler;
    private final TelegramMessageSender telegramMessageSender;
    private final Map<Long, BotState> userStates = new HashMap<>();
    private static final ResourceBundle messageResourceBundle = loadResourceBundle();
    private static final ResourceBundle errorResourceBundle = loadErrorResourceBundle();
    private static final String START_COMMAND = "/start";
    private static final String HELP_COMMAND = "/help";
    private static final String EXIT_COMMAND = "/exit";
    private static final String TOMORROW_COMMAND = "tomorrow";

    public TelegramBotDialogHandlerImpl(TelegramBotRequestHandler telegramBotRequestHandler, TelegramMessageSenderImpl telegramMessageSender) {
        this.telegramBotRequestHandler = telegramBotRequestHandler;
        this.telegramMessageSender = telegramMessageSender;
    }

    /**
     * Handles an incoming update.
     *
     * @param update The update from Telegram.
     */
    @Override
    public void handleUpdate(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        if (!userStates.containsKey(userId)) {
            userStates.put(userId, new BotState());
        }
        BotState botState = userStates.get(userId);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            if (START_COMMAND.equals(messageText)) {
                log.info("Received '/start' command from user.");
                startNewConversation(update);
                botState = initializeBot(update);
            } else if (HELP_COMMAND.equals(messageText)) {
                log.info("Received '/help' command from user.");
                showHelp(botState, update);
            } else if (EXIT_COMMAND.equals(messageText)) {
                log.info("Received '/exit' command from user.");
                userStates.remove(userId);
                telegramMessageSender.exitProgram(messageResourceBundle.getString("exit"), update);
            } else {
                log.info("Received a user message: '{}'", messageText);
                handleUserMessage(messageText, botState, update);
            }
        }
    }

    private void handleUserMessage(String messageText, BotState botState, Update update) {
        switch (botState.getState()) {
            case WAITING_FOR_CURRENCY:
                handleCurrencySelection(messageText, botState, update, botState.getCurrencies());
                break;
            case WAITING_FOR_PERIOD:
                handlePeriodSelection(messageText, botState, update);
                break;
            case WAITING_FOR_DATE_INPUT:
                handleDateInput(messageText, botState, update);
                break;
            case WAITING_FOR_ALGORITHM:
                handleAlgorithmSelection(messageText, botState, update);
                break;
            case WAITING_FOR_OUTPUT:
                handleOutputSelection(messageText, botState);
                handleRequestAndRestart(botState, update);
                botState = initializeBot(update);
                break;
        }
    }

    private BotState initializeBot(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        if (!userStates.containsKey(userId)) {
            userStates.remove(userId);
        }
        BotState botState = new BotState();
        botState.setState(WAITING_FOR_CURRENCY);
        telegramBotRequestHandler.openCurrencyKeyboard(update, Collections.emptyList());
        userStates.put(userId, botState);
        return botState;
    }

    private void startNewConversation(Update update) {
        String instructions = messageResourceBundle.getString("welcome.message");
        telegramMessageSender.sendMessage(instructions, update);
    }

    private void handleCurrencySelection(String messageText, BotState botState, Update update, List<Currency> currencies) {
        if (!Objects.equals(messageText, messageResourceBundle.getString("choose.rate.type.message"))) {
            botState.getCurrencies().add(Currency.valueOf(messageText));
            botState.setState(WAITING_FOR_CURRENCY);
            telegramBotRequestHandler.openCurrencyKeyboard(update, currencies);
        } else {
            botState.setState(WAITING_FOR_PERIOD);
            telegramBotRequestHandler.openRateTypeKeyboard(update);
        }
    }

    private void handlePeriodSelection(String messageText, BotState botState, Update update) {
        if (messageText.equals(messageResourceBundle.getString("manual.date.input.message"))) {
            botState.setState(State.WAITING_FOR_DATE_INPUT);
        } else {
            botState.setPeriod(RateType.valueOf(messageText));
            botState.setState(State.WAITING_FOR_ALGORITHM);
            telegramBotRequestHandler.openAlgorithmKeyboard(update);
        }
    }

    private void handleDateInput(String messageText, BotState botState, Update update) {
        if (messageText.equals(TOMORROW_COMMAND)) {
            botState.setDate(DateUtils.getTomorrowDate());
            botState.setState(State.WAITING_FOR_ALGORITHM);
            telegramBotRequestHandler.openAlgorithmKeyboard(update);
        } else {
            try {
                botState.setDate(DateUtils.parseDate(messageText));
                botState.setState(State.WAITING_FOR_ALGORITHM);
                telegramBotRequestHandler.openAlgorithmKeyboard(update);
            } catch (RuntimeException e) {
                log.error("Date parse error: {}", e.getMessage());
                telegramMessageSender.sendMessage(errorResourceBundle.getString("date.parse.error.message"), update);
                botState.setState(WAITING_FOR_DATE_INPUT);
            }

        }
    }

    private void handleAlgorithmSelection(String messageText, BotState botState, Update update) {
        botState.setAlgorithm(ForecastingAlgorithm.valueOf(messageText));
        botState.setState(State.WAITING_FOR_OUTPUT);
        if (isNull(botState.getPeriod())) {
            botState.setPeriod(RateType.DAY);
        }
        telegramBotRequestHandler.openOutputKeyboard(update, botState.getPeriod());
    }

    private void handleOutputSelection(String messageText, BotState botState) {
        botState.setOutputType(OutputType.valueOf(messageText));
    }

    private void handleRequestAndRestart(BotState botState, Update update) {
        FormattedResult result = telegramMessageSender.handleUserRequest(botState);
        telegramMessageSender.sendMessageResult(result, update);
        telegramMessageSender.sendMessage(messageResourceBundle.getString("success.message"), update);
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

    private void showHelp(BotState botState, Update update) {
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

        telegramMessageSender.sendMessage(helpMessage.toString(), update);
    }
}
