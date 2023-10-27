package ru.liga.rateforecaster.parser;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;


/**
 * This class provides methods for reading user input from the console.
 */
public class ConsoleInputReader {
    private final ResourceBundle resourceBundle;
    private final Scanner scanner;


    public ConsoleInputReader(String locale) {
        scanner = new Scanner(System.in);
        this.resourceBundle = ResourceBundle.getBundle("messages/messages", new Locale(locale));
    }

    /**
     * Reads a line of text from the console input.
     *
     * @return The user's input as a string.
     */
    public String readConsoleInput() {
        System.out.print(resourceBundle.getString("request_text"));
        return scanner.nextLine();
    }
}