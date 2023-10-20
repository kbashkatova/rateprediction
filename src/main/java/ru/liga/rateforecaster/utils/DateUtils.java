package ru.liga.rateforecaster.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for working with dates.
 */
public class DateUtils {

    private static final int ONE_DAY = 1;
    private static final int WEEK = 7;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    /**
     * Get the date for tomorrow.
     *
     * @return The date for tomorrow.
     */
    public static LocalDate getTomorrowDate() {
        return LocalDate.now().plusDays(ONE_DAY);
    }


    /**
     * Get the current date.
     *
     * @return The current date.
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * Get the date for the last day of the week for forecasting.
     *
     * @return The date for the last day of the week for forecasting.
     */
    public static LocalDate getLastDayOfWeekForecast() {
        return DateUtils.getCurrentDate().plusDays(WEEK);
    }


    /**
     * Parse a date string into a LocalDate object.
     *
     * @param dateStr The date string to parse.
     * @return A LocalDate object parsed from the date string.
     */
    public static LocalDate formatDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    /**
     * Get the next date following the given date.
     *
     * @param currentDate The current date.
     * @return The next date.
     */
    public static LocalDate getNextDate(LocalDate currentDate) {
        return currentDate.plusDays(ONE_DAY);
    }
}
