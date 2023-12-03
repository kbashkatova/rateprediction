package ru.liga.rateforecaster.forecast.algorithm.mist;

import java.util.Random;


/**
 * The DefaultRandomNumberGenerator class is an implementation of the {@link RandomNumberGenerator} interface.
 * It provides random number generation functionality using the Java built-in Random class.
 */
class DefaultRandomNumberGenerator implements RandomNumberGenerator {
    private final Random random = new Random();

    /**
     * Generates a random integer within the specified bound.
     *
     * @param bound The upper bound for the generated random integer (exclusive).
     * @return A random integer within the specified bound.
     */
    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}




