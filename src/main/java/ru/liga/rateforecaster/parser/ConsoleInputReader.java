package ru.liga.rateforecaster.parser;

import java.util.Scanner;


/**
 * This class provides methods for reading user input from the console.
 */
public class ConsoleInputReader {
    private final String REQUEST_TEXT =  "Введите запрос (для завершения программы введите \"exit\"): ";
    private final Scanner scanner;


    public ConsoleInputReader() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of text from the console input.
     *
     * @return The user's input as a string.
     */
    public String readConsoleInput() {
        System.out.print(REQUEST_TEXT);
        return scanner.nextLine();
    }
}