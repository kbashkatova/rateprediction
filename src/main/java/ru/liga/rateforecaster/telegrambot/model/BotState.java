package ru.liga.rateforecaster.telegrambot.model;

import lombok.Getter;
import lombok.Setter;
import ru.liga.rateforecaster.enums.Algorithm;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.enums.OutputType;
import ru.liga.rateforecaster.enums.RateType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the state of the Telegram bot conversation and user selections.
 */
@Getter
@Setter
public class BotState {
    private List<Currency> currencies = new ArrayList<>();
    private RateType period;
    private Optional<LocalDate> date = Optional.empty();
    private Algorithm algorithm;
    private OutputType outputType;
    private State state;
}
