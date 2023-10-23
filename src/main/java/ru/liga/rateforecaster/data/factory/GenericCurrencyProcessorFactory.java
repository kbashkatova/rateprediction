package ru.liga.rateforecaster.data.factory;

import ru.liga.rateforecaster.data.processor.CurrencyDataProcessor;
import ru.liga.rateforecaster.enums.Currency;

/**
 * A factory for creating instances of CurrencyDataProcessor.
 */
public class GenericCurrencyProcessorFactory implements CurrencyProcessorFactory {
    private final String filePath;

    public GenericCurrencyProcessorFactory(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public CurrencyDataProcessor createCurrencyDataProcessor() {
        return new CurrencyDataProcessor(filePath);
    }

    /**
     * Gets a factory for creating CurrencyDataProcessor instances based on the specified currency.
     *
     * @param currency the currency for which to create a factory
     * @return a CurrencyProcessorFactory for the specified currency
     */
    public static CurrencyProcessorFactory getFactory(Currency currency) {
        final Currency currencyEnum = Currency.valueOf(currency.getCode());
        return new GenericCurrencyProcessorFactory(currencyEnum.getFilePath());
    }
}