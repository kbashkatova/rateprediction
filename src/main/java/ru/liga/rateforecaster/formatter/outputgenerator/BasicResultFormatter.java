package ru.liga.rateforecaster.formatter.outputgenerator;

import ru.liga.rateforecaster.utils.AppConfig;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An abstract class representing a basic result formatter for generating output.
 */
public abstract class BasicResultFormatter {
    final ResourceBundle resourceBundle;


    /**
     * Constructs a BasicResultFormatter with the provided resource bundle.
     *
     * @param resourceBundle The resource bundle for localization.
     */
    protected BasicResultFormatter(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
}
