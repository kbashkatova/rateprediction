package ru.liga.rateforecaster.data.factory;

import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.utils.AppConfig;

public class CurrencyPathResolver {
    private final AppConfig appConfig;

    public CurrencyPathResolver(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String getPath(Currency currency) {
        switch (currency) {
            case EUR:
                return appConfig.getEurFilePath();
            case TRY:
                return appConfig.getTryFilePath();
            case USD:
                return appConfig.getUsdFilePath();
            case BGN:
                return appConfig.getBgnFilePath();
            case AMD:
                return appConfig.getAmdFilePath();
            default:
                throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
    }
}
