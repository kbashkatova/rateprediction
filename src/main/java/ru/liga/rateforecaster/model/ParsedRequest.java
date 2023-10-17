package ru.liga.rateforecaster.model;

/**
 * A class representing a parsed user request for a currency forecast.
 */
public class ParsedRequest {
    private RateType rateType;
    private Currency currency;

    /**
     * Constructs a new ParsedRequest with the provided rate type and currency.
     *
     * @param rateType The rate type requested in the user's query.
     * @param currency The currency for which the forecast is requested.
     */
    public ParsedRequest(RateType rateType, Currency currency) {
        this.rateType = rateType;
        this.currency = currency;
    }

    public RateType getRateType() {
        return rateType;
    }


    public Currency getCurrency() {
        return currency;
    }
}
