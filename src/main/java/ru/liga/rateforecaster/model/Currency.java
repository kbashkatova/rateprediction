package ru.liga.rateforecaster.model;

/**
 * An enum representing currency types.
 */
public enum Currency {
    EUR("EUR", "Евро", "/EUR.csv"),
    TRY("TRY", "Турецкая лира", "/TRY.csv"),
    USD("USD", "Доллар США", "/USD.csv");

    private final String code;
    private final String name;
    private final String filePath;

    Currency(String code, String name, String filePath) {
        this.code = code;
        this.name = name;
        this.filePath = filePath;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }
}