package ru.liga.rateforecaster.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class for working with dates.
 */
public class DateUtils {

    private static final int ONE_DAY = 1;
    private static final int ONE_MONTH = 1;
    private static final int ONE_YEAR = 1;
    private static final int WEEK = 7;

    public static LocalDate getTomorrowDate() {
        return LocalDate.now().plusDays(ONE_DAY);
    }


    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static LocalDate getLastDayOfWeekForecast(LocalDate targetDate) {
        return targetDate.plusDays(WEEK);
    }

    public static LocalDate getLastDayOfMonthForecast(LocalDate targetDate) {
        return targetDate.plusMonths(ONE_MONTH);
    }

    public static LocalDate getLastYearDate(LocalDate localDate) {
        return localDate.minusYears(ONE_YEAR);}


    public static LocalDate formatDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    public static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try {
            return LocalDate.parse(dateString, formatter);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFormattedDateForOutput(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd.MM.yyyy",
                new Locale(AppConfig.getInstance().getLocale()));
        return date.format(formatter);
    }

    public static LocalDate getNextDate(LocalDate currentDate) {
        return currentDate.plusDays(ONE_DAY);
    }

    public static LocalDate getPreviousDate(LocalDate currentDate) {
        return currentDate.minusDays(ONE_DAY);
    }

    public static LocalDate getLastMonthDate(LocalDate date) {
        return date.minusMonths(ONE_MONTH);
    }
}
