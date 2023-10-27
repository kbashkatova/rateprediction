package ru.liga.rateforecaster;

import ru.liga.rateforecaster.forecast.UserRequestForecastGenerator;


/**
 * This is the main class responsible for running the currency forecast application.
 */
public class Application {
    public static void main(String[] args) {
        while (true) {
            System.out.println(UserRequestForecastGenerator.proceedUserRequest());
        }
    }
}