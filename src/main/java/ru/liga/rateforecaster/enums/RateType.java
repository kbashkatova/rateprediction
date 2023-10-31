package ru.liga.rateforecaster.enums;

/**
 * An enumeration representing different rate types for currency forecasting.
 */
public enum RateType {
    DAY("day"),
    WEEK("week"),
    MONTH("month");

    private final String description;


    RateType(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }
}
