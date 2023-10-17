package ru.liga.rateforecaster.model;

/**
 * An enumeration representing different rate types for currency forecasting.
 */
public enum RateType {
    TOMORROW("tomorrow"),
    WEEK("week");

    private String description;


    RateType(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }
}
