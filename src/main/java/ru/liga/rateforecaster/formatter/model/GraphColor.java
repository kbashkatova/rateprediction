package ru.liga.rateforecaster.formatter.model;

import lombok.Getter;

import java.awt.*;
import java.util.Random;

/**
 * Enum representing various colors for chart data series in the graphical output.
 */
@Getter
public enum GraphColor {

    RED(Color.RED),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    ORANGE(Color.ORANGE),
    MAGENTA(Color.MAGENTA),
    CYAN(Color.CYAN),
    PINK(Color.PINK);

    private final Paint paint;

    GraphColor(Paint paint) {
        this.paint = paint;
    }

    /**
     * Get a random color from the enum.
     *
     * @return A random {@link Paint} color.
     */
    public static Paint getRandomColor() {
        GraphColor[] values = GraphColor.values();
        int randomIndex = new Random().nextInt(values.length);
        return values[randomIndex].getPaint();
    }
}

