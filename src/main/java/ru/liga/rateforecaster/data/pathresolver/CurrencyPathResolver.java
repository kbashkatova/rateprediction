package ru.liga.rateforecaster.data.pathresolver;

import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.utils.AppConfig;

public class CurrencyPathResolver {
    private final AppConfig appConfig;

    public CurrencyPathResolver(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String getPath(Currency currency) {
        return switch (currency) {
            case EUR -> appConfig.getEurFilePath();
            case TRY -> appConfig.getTryFilePath();
            case USD -> appConfig.getUsdFilePath();
            case BGN -> appConfig.getBgnFilePath();
            case AMD -> appConfig.getAmdFilePath();
            default -> throw new IllegalArgumentException("Unsupported currency: " + currency);
        };
    }
}
