package ru.liga.rateforecaster.model;

import ru.liga.rateforecaster.utils.AppConfig;

/**
 * An enum representing currency types.
 */
public enum Currency {
    EUR("EUR", AppConfig.getInstance().getEurFilePath()),
    TRY("TRY",  AppConfig.getInstance().getTryFilePath()),
    USD("USD", AppConfig.getInstance().getUsdFilePath());

    private final String code;
    private final String filePath;

    Currency(String code, String filePath) {
        this.code = code;
        this.filePath = filePath;
    }

    public String getCode() {
        return code;
    }


    public String getFilePath() {
        return filePath;
    }
}