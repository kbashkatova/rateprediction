package ru.liga.rateforecaster.forecast.algorithm.mist;


/**
 * The RandomNumberGenerator interface represents a random number generator.
 */
public interface RandomNumberGenerator {

    /**
     * Generates a random integer within the specified bound.
     *
     * @param bound The upper bound for the generated random integer (exclusive).
     * @return A random integer within the specified bound.
     */
    int nextInt(int bound);
}