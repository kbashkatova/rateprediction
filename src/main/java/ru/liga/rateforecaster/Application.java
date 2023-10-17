package ru.liga.rateforecaster;

import com.opencsv.exceptions.CsvValidationException;
import ru.liga.rateforecaster.forecast.UserRequestForecastGenerator;
import ru.liga.rateforecaster.parser.ConsoleInputReader;

import java.io.IOException;
import java.text.ParseException;


/**
 * This is the main class responsible for running the currency forecast application.
 */
public class Application {
    public static void main(String[] args) {
        
        while (true) {
            System.out.println(UserRequestForecastGenerator.generateForecast());
        }
    }
}