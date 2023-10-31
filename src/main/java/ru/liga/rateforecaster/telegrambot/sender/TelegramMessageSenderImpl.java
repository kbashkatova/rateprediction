package ru.liga.rateforecaster.telegrambot.sender;

import org.jfree.chart.ChartPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.rateforecaster.forecast.UserRequestForecastGenerator;
import ru.liga.rateforecaster.model.FormattedResult;
import ru.liga.rateforecaster.telegrambot.Bot;
import ru.liga.rateforecaster.telegrambot.model.BotState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * The implementation of the TelegramMessageSender interface for sending messages in a Telegram bot.
 */
public class TelegramMessageSenderImpl implements TelegramMessageSender {

    private final Bot bot;
    private final ResourceBundle resourceBundle;
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    public TelegramMessageSenderImpl(Bot bot, ResourceBundle resourceBundle) {
        this.bot = bot;
        this.resourceBundle = resourceBundle;
    }

    /**
     * Handles a user request and generates a formatted result.
     *
     * @param botState The current state of the bot.
     * @return A formatted result based on the user's request.
     */
    public FormattedResult handleUserRequest(BotState botState) {
        return UserRequestForecastGenerator.proceedUserRequest(botState);
    }

    /**
     * Sends a formatted result message to the user.
     *
     * @param result The formatted result to send.
     * @param update The update object to reply to.
     */
    public void sendMessageResult(FormattedResult result, Update update) {
        if (result.getTextResult() != null) {
            sendMessage(result.getTextResult(), update);
        } else if (result.getChartImage() != null) {
            try {
                sendChart(result.getChartImage(), update);
            } catch (IOException e) {
                log.error("Failed to send chart", e);
                sendError("error.failedToSendChart", update.getMessage().getChatId());
            }
        } else if (result.getErrorMessage() != null) {
            sendMessage(result.getErrorMessage().getErrorMessageText(), update);
        }
    }

    /**
     * Sends a text message to the user.
     *
     * @param message The text message to send.
     * @param update  The update object to reply to.
     */
    public void sendMessage(String message, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText(message);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send message", e);
        }
    }

    /**
     * Sends a chart as a photo to the user.
     *
     * @param chartPanel The chart to send as a photo.
     * @param update     The update object to reply to.
     * @throws IOException if there's an issue with image conversion.
     */
    private void sendChart(ChartPanel chartPanel, Update update) throws IOException {
        int width = 800;
        int height = 600;
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(update.getMessage().getChatId());
        BufferedImage bufferedImage = chartPanel.getChart().createBufferedImage(width, height);
        InputFile inputFile = new InputFile(new ByteArrayInputStream(bufferedImageToByteArray(bufferedImage)), "image.png");
        sendPhoto.setPhoto(inputFile);
        try {
            bot.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Failed to send chart", e);
        }
    }

    /**
     * Converts a BufferedImage to a byte array.
     *
     * @param bufferedImage The BufferedImage to convert.
     * @return A byte array containing the image data.
     * @throws IOException if there's an issue with image conversion.
     */
    private byte[] bufferedImageToByteArray(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }

    /**
     * Sends an error message to the user with a specified chat ID.
     *
     * @param errorMessage The error message to send.
     * @param chatId       The chat ID of the user to send the error message to.
     */
    public void sendError(String errorMessage, Long chatId) {
        String localizedErrorMessage = resourceBundle.getString(errorMessage);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(localizedErrorMessage);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send error message", e);
        }
    }
}