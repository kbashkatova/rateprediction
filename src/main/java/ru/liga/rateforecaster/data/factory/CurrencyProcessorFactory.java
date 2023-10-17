package ru.liga.rateforecaster.data.factory;

import ru.liga.rateforecaster.data.processor.CurrencyDataProcessor;

public interface CurrencyProcessorFactory {
    CurrencyDataProcessor createCurrencyDataProcessor();

}